(function () {
    'use strict';

    var body = document.body;
    var formTurista = document.getElementById('form-turista');
    var formProveedor = document.getElementById('form-proveedor');
    var cuerpo = document.getElementById('proveedor-cuerpo');
    var listaTours = document.getElementById('tours-lista');
    var plantilla = document.getElementById('tour-template');
    var btnAgregar = document.getElementById('tour-agregar');
    var tabs = document.querySelectorAll('[data-rol-tab]');

    // ---------- Turista / Proveedor ----------
    function setRol(rol) {
        var esProveedor = rol === 'proveedor';
        formTurista.hidden = esProveedor;
        formProveedor.hidden = !esProveedor;
        tabs.forEach(function (tab) {
            tab.setAttribute('aria-selected', String(tab.dataset.rolTab === rol));
        });
    }

    tabs.forEach(function (tab) {
        tab.addEventListener('click', function () {
            setRol(tab.dataset.rolTab);
        });
    });

    function aplicarTipo() {
        var elegido = formProveedor.querySelector('input[name="tipo"]:checked');
        var tipo = elegido ? elegido.value : null;

        cuerpo.hidden = !tipo;

        cuerpo.querySelectorAll('input, select, textarea, button').forEach(function (el) {
            el.disabled = !tipo;
        });

        cuerpo.querySelectorAll('[data-tipos]').forEach(function (grupo) {
            var visible = !!tipo && grupo.dataset.tipos.split(',').indexOf(tipo) !== -1;
            grupo.hidden = !visible;
            grupo.querySelectorAll('input, select, textarea').forEach(function (el) {
                el.disabled = !visible;
            });
        });

        if (tipo && listaTours.children.length === 0) {
            agregarTour();
        }
    }

    formProveedor.querySelectorAll('input[name="tipo"]').forEach(function (radio) {
        radio.addEventListener('change', aplicarTipo);
    });

    function renumerarTours() {
        var tours = listaTours.querySelectorAll('.registro-tour');
        tours.forEach(function (tour, i) {
            tour.querySelector('.registro-tour-titulo').textContent = 'Tour ' + (i + 1);
            tour.querySelectorAll('[name]').forEach(function (el) {
                el.name = el.name.replace(/^tours\[[^\]]*\]/, 'tours[' + i + ']');
            });
            tour.querySelector('.registro-tour-quitar').hidden = tours.length === 1;
        });
    }

    function agregarTour() {
        var copia = plantilla.content.cloneNode(true);
        listaTours.appendChild(copia);
        renumerarTours();
    }

    btnAgregar.addEventListener('click', agregarTour);

    listaTours.addEventListener('click', function (e) {
        var boton = e.target.closest('.registro-tour-quitar');
        if (!boton) return;
        boton.closest('.registro-tour').remove();
        renumerarTours();
    });

    function validarIdiomas(form) {
        var cajas = form.querySelectorAll('input[name="idiomas"]:not(:disabled)');
        if (!cajas.length) return;
        var alguno = Array.prototype.some.call(cajas, function (c) { return c.checked; });
        cajas[0].setCustomValidity(alguno ? '' : 'Selecciona al menos un idioma');
    }

    [formTurista, formProveedor].forEach(function (form) {
        form.addEventListener('change', function (e) {
            if (e.target.name === 'idiomas') validarIdiomas(form);
        });
        form.addEventListener('submit', function () {
            validarIdiomas(form);
        }, true);
        validarIdiomas(form);
    });

    // ---------- Estado inicial ----------
    setRol(body.dataset.rol === 'proveedor' ? 'proveedor' : 'turista');

    var tipoInicial = body.dataset.tipo;
    if (tipoInicial) {
        var radio = formProveedor.querySelector('input[name="tipo"][value="' + tipoInicial + '"]');
        if (radio) radio.checked = true;
    }
    aplicarTipo();
})();