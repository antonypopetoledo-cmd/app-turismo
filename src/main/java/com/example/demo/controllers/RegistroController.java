package com.example.demo.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    // ---------- Catálogos ----------
    private static final List<String> TIPOS_PROVEEDOR = List.of("EMPRESA", "INDEPENDIENTE", "ONG");
    private static final List<String> SEXOS = List.of("F", "M", "O");
    private static final List<String> IDIOMAS = List.of(
            "Español", "Inglés", "Francés", "Alemán", "Portugués",
            "Italiano", "Chino", "Japonés", "Quechua", "Otro");

    // ---------- Patrones de validación ----------
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PASAPORTE = Pattern.compile("^[A-Za-z0-9]{6,12}$");
    private static final Pattern CELULAR = Pattern.compile("^\\+?[0-9 ]{7,15}$");
    private static final Pattern DOCUMENTO = Pattern.compile("^[A-Za-z0-9]{8,12}$"); // DNI o carné de extranjería
    private static final Pattern RUC = Pattern.compile("^[0-9]{11}$");
    private static final Pattern URL = Pattern.compile("^https?://\\S+\\.\\S+$");
    private static final Pattern HORA = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");

    /**
     * Permite enlazar los campos de los formularios (incluida la lista de tours
     * "tours[0].nombre", "tours[1].nombre"...) sin necesidad de getters/setters.
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    // =====================================================
    //  FORMULARIOS
    // =====================================================

    public static class TuristaForm {
        public String email;
        public String nombre;
        public String apellido;
        public String password;
        public String pais;
        public List<String> idiomas = new ArrayList<>();
        public String sexo;
        public String pasaporte;
        public String celular; // opcional
    }

    public static class TourForm {
        public String nombre;
        public String itinerario;
        public String mapa;
        public String lugarInicio;
        public String horaInicio;
        public String lugarDestino;
        public String duracionHoras;
        public String aforo;
        public String precio;
        public String costosAdicionales;
        public String incluye;          // una opción por línea
        public String excluye;          // una exclusión por línea
        public String recomendaciones;  // una necesidad recomendada por línea
        public String reconocimiento;   // un punto de reconocimiento previo por línea
        public List<MultipartFile> imagenes = new ArrayList<>();
    }

    public static class ProveedorForm {
        public String tipo;               // EMPRESA | INDEPENDIENTE | ONG
        public String email;
        public String marca;              // solo EMPRESA y ONG
        public String nombre;             // solo guía (INDEPENDIENTE)
        public String apellido;           // solo guía
        public String password;
        public String pais;
        public List<String> idiomas = new ArrayList<>();
        public String identificadorGuia;  // solo guía
        public String documento;          // solo guía: DNI o carné de extranjería
        public String ruc;                // RUC / RUS
        public String celular;
        public String urlNegocio;
        public List<TourForm> tours = new ArrayList<>();
    }

    // =====================================================
    //  VISTA
    // =====================================================

    @GetMapping({"", "/"})
    public String registro(
            @RequestParam(required = false) String rol,
            HttpSession session,
            Model model
    ) {
        if (session.getAttribute("userName") != null) {
            return "redirect:/destinos";
        }
        cargarListas(model);
        model.addAttribute("rol", "proveedor".equalsIgnoreCase(rol) ? "proveedor" : "turista");
        return "registro";
    }

    // =====================================================
    //  REGISTRO DE TURISTA
    // =====================================================

    @PostMapping("/turista")
    public String registrarTurista(
            @ModelAttribute("form") TuristaForm f,
            RedirectAttributes redirect,
            Model model
    ) {
        List<String> errores = new ArrayList<>();

        if (vacio(f.email) || !EMAIL.matcher(f.email.trim()).matches()) {
            errores.add("Ingresa un correo electrónico válido.");
        }
        if (vacio(f.nombre)) errores.add("El nombre es obligatorio.");
        if (vacio(f.apellido)) errores.add("El apellido es obligatorio.");
        if (vacio(f.password) || f.password.length() < 8) {
            errores.add("La contraseña debe tener al menos 8 caracteres.");
        }
        if (vacio(f.pais) || !paises().contains(f.pais)) errores.add("Selecciona tu país.");
        validarIdiomas(f.idiomas, errores);
        if (vacio(f.sexo) || !SEXOS.contains(f.sexo)) errores.add("Selecciona tu sexo.");
        if (vacio(f.pasaporte) || !PASAPORTE.matcher(f.pasaporte.trim()).matches()) {
            errores.add("Ingresa un número de pasaporte válido (6 a 12 letras o números).");
        }
        // El celular es opcional: solo se valida si el turista lo escribió.
        if (!vacio(f.celular) && !CELULAR.matcher(f.celular.trim()).matches()) {
            errores.add("El número de celular no es válido.");
        }

        if (!errores.isEmpty()) {
            return volverConErrores("turista", null, errores, model);
        }

        // TODO: guardar el turista en la base de datos
        //       (cifrando la contraseña con BCrypt, nunca en texto plano).

        redirect.addFlashAttribute("exito", "¡Registro completado! Ya puedes iniciar sesión.");
        return "redirect:/login/";
    }

    // =====================================================
    //  REGISTRO DE PROVEEDOR DE ACTIVIDADES
    // =====================================================

    @PostMapping("/proveedor")
    public String registrarProveedor(
            @ModelAttribute("form") ProveedorForm f,
            RedirectAttributes redirect,
            Model model
    ) {
        List<String> errores = new ArrayList<>();

        // Solo se puede escoger una de las tres opciones.
        if (vacio(f.tipo) || !TIPOS_PROVEEDOR.contains(f.tipo)) {
            errores.add("Selecciona el tipo de proveedor: empresa, independiente u ONG.");
            return volverConErrores("proveedor", null, errores, model);
        }

        boolean esGuia = "INDEPENDIENTE".equals(f.tipo);
        boolean esEmpresaOng = !esGuia;

        // --- Datos para todos los tipos ---
        if (vacio(f.email) || !EMAIL.matcher(f.email.trim()).matches()) {
            errores.add("Ingresa un correo electrónico válido.");
        }
        if (vacio(f.password) || f.password.length() < 8) {
            errores.add("La contraseña debe tener al menos 8 caracteres.");
        }
        if (vacio(f.pais) || !paises().contains(f.pais)) errores.add("Selecciona tu país.");
        validarIdiomas(f.idiomas, errores);
        if (vacio(f.ruc) || !RUC.matcher(f.ruc.trim()).matches()) {
            errores.add("El RUC/RUS debe tener 11 dígitos.");
        }
        if (vacio(f.celular) || !CELULAR.matcher(f.celular.trim()).matches()) {
            errores.add("Ingresa un número de celular válido.");
        }
        if (vacio(f.urlNegocio) || !URL.matcher(f.urlNegocio.trim()).matches()) {
            errores.add("Ingresa la URL de tu negocio (debe empezar con http:// o https://).");
        }

        // --- Solo empresa y ONG ---
        if (esEmpresaOng && vacio(f.marca)) {
            errores.add("La marca o nombre público es obligatorio.");
        }

        // --- Solo guía (independiente) ---
        if (esGuia) {
            if (vacio(f.nombre)) errores.add("El nombre es obligatorio.");
            if (vacio(f.apellido)) errores.add("El apellido es obligatorio.");
            if (vacio(f.identificadorGuia)) {
                errores.add("El identificador de registro como guía es obligatorio.");
            }
            if (vacio(f.documento) || !DOCUMENTO.matcher(f.documento.trim()).matches()) {
                errores.add("Ingresa un DNI o carné de extranjería válido.");
            }
        }

        // --- Tours ---
        if (f.tours == null || f.tours.isEmpty()) {
            errores.add("Registra al menos un tour.");
        } else {
            for (int i = 0; i < f.tours.size(); i++) {
                validarTour(f.tours.get(i), "Tour " + (i + 1), errores);
            }
        }

        if (!errores.isEmpty()) {
            return volverConErrores("proveedor", f.tipo, errores, model);
        }

        // TODO: guardar el proveedor y sus tours en la base de datos y las imágenes
        //       en disco o almacenamiento externo. Los comentarios y la calificación
        //       NO se registran aquí: los dejan los turistas y se muestran en el
        //       perfil público del proveedor.

        redirect.addFlashAttribute("exito", "¡Registro completado! Ya puedes iniciar sesión.");
        return "redirect:/login/";
    }

    // =====================================================
    //  AUXILIARES
    // =====================================================

    private void validarTour(TourForm t, String etiqueta, List<String> errores) {
        String p = etiqueta + ": ";

        if (vacio(t.nombre)) errores.add(p + "el nombre del tour es obligatorio.");
        if (vacio(t.itinerario)) errores.add(p + "el itinerario es obligatorio.");
        if (vacio(t.mapa) || !URL.matcher(t.mapa.trim()).matches()) {
            errores.add(p + "el mapa debe ser un enlace válido (http:// o https://).");
        }
        if (vacio(t.lugarInicio)) errores.add(p + "el lugar de inicio o partida es obligatorio.");
        if (vacio(t.horaInicio) || !HORA.matcher(t.horaInicio.trim()).matches()) {
            errores.add(p + "la hora de inicio o partida no es válida.");
        }
        if (vacio(t.lugarDestino)) errores.add(p + "el lugar de destino o llegada es obligatorio.");

        BigDecimal duracion = numero(t.duracionHoras);
        if (duracion == null || duracion.signum() <= 0) {
            errores.add(p + "la duración debe ser mayor a 0 horas.");
        }
        BigDecimal aforo = numero(t.aforo);
        if (aforo == null || aforo.signum() <= 0 || aforo.stripTrailingZeros().scale() > 0) {
            errores.add(p + "el aforo debe ser un número entero mayor a 0.");
        }
        BigDecimal precio = numero(t.precio);
        if (precio == null || precio.signum() < 0) {
            errores.add(p + "el precio por turista no es válido.");
        }

        if (vacio(t.costosAdicionales)) errores.add(p + "indica los costos adicionales (escribe \"Ninguno\" si no hay).");
        if (vacio(t.incluye)) errores.add(p + "la lista de opciones incluidas es obligatoria.");
        if (vacio(t.excluye)) errores.add(p + "la lista de exclusiones es obligatoria.");
        if (vacio(t.recomendaciones)) errores.add(p + "la lista de necesidades recomendadas es obligatoria.");
        if (vacio(t.reconocimiento)) errores.add(p + "la lista de reconocimiento previo es obligatoria.");

        long imagenesValidas = t.imagenes == null ? 0 : t.imagenes.stream()
                .filter(img -> img != null && !img.isEmpty())
                .filter(img -> img.getContentType() != null && img.getContentType().startsWith("image/"))
                .count();
        if (imagenesValidas == 0) {
            errores.add(p + "sube al menos una imagen (JPG, PNG o WEBP).");
        }
    }

    private void validarIdiomas(List<String> seleccionados, List<String> errores) {
        if (seleccionados == null || seleccionados.isEmpty()) {
            errores.add("Selecciona al menos un idioma.");
        } else if (!IDIOMAS.containsAll(seleccionados)) {
            errores.add("Hay un idioma no válido en tu selección.");
        }
    }

    private String volverConErrores(String rol, String tipo, List<String> errores, Model model) {
        cargarListas(model);
        model.addAttribute("errores", errores);
        model.addAttribute("rol", rol);
        model.addAttribute("tipoSeleccionado", tipo);
        return "registro";
    }

    private void cargarListas(Model model) {
        model.addAttribute("paises", paises());
        model.addAttribute("idiomas", IDIOMAS);
    }

    /** Lista de países en español, ordenada alfabéticamente. */
    private List<String> paises() {
        Locale es = Locale.forLanguageTag("es");
        Collator collator = Collator.getInstance(es);
        return Arrays.stream(Locale.getISOCountries())
                .map(codigo -> new Locale.Builder().setRegion(codigo).build().getDisplayCountry(es))
                .filter(nombre -> !nombre.isBlank())
                .sorted(collator)
                .toList();
    }

    private boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    private BigDecimal numero(String s) {
        if (vacio(s)) return null;
        try {
            return new BigDecimal(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}