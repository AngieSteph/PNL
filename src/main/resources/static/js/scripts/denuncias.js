const url = "/v1/denuncias";

function ajaxRequest(type, endpoint, data = null) {
    return $.ajax({
        type,
        url: endpoint,
        data: data ? JSON.stringify(data) : null,
        dataType: "json",
        contentType: data ? "application/json" : undefined,
        cache: false,
        timeout: 600000,
    });
}

function save(isNew) {
    const id = $("#guardar").data("id");
    const denuncia = {
        id: isNew ? null : id,  
        titulo: $("#titulo").val(),
        descripcion: $("#descripcion").val(),
        ubicacion: $("#ubicacion").val(),
        estado: $("#estado").val(),
        ciudadano: $("#ciudadano").val(),
        telefonoCiudadano: $("#telefono").val()
    };

    const method = isNew ? "POST" : "PUT";
    const endpoint = isNew ? url : `${url}/${id}`;

    $.ajax({
        type: method,
        url: endpoint,
        data: JSON.stringify(denuncia),
        contentType: "application/json",
        success: (data) => {
            if (data.ok) {
                $("#modal-update").modal("hide");
                getTabla();  // Llamar para actualizar la tabla de denuncias
                Swal.fire({
                    icon: 'success',
                    title: `Denuncia ${isNew ? 'creada' : 'actualizada'} con éxito`,
                    showConfirmButton: false,
                    timer: 1500
                });
                clearForm();
            } else {
                showError(data.message);
            }
        },
        error: (jqXHR) => {
            const errorMessage = jqXHR.responseJSON?.message || `Error inesperado: ${jqXHR.status}`;
            showError(errorMessage);
        }
    });
}

function showError(message) {
    $("#error-message").text(message).removeClass("d-none");
}

function deleteFila(id) {
    ajaxRequest("DELETE", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Se ha eliminado el registro',
                    showConfirmButton: false,
                    timer: 1500
                });
                getTabla();
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.message,
                    confirmButtonText: 'Aceptar'
                });
            }
        })
        .fail(handleError);
}

function getTabla() {
    ajaxRequest("GET", url)
        .done((data) => {
            const t = $("#tablaDenuncias").DataTable();
            t.clear().draw(false);

            if (data.ok) {
                $.each(data.body, (index, registro) => {
                    const botonera = `
                        <button type="button" class="btn btn-warning btn-xs editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-xs eliminar">
                            <i class="fas fa-trash"></i>
                        </button>`;
                    t.row.add([
                        botonera, 
                        registro.id, 
                        registro.titulo, 
                        registro.descripcion, 
                        registro.ubicacion, 
                        registro.estado, 
                        registro.ciudadano, 
                        registro.telefonoCiudadano,
                        registro.fechaRegistro
                    ]);
                });
                t.draw(false);
            } else {
                console.error("Error en la respuesta: ", data.message);
            }
        })
        .fail(handleError);
}

function getFila(id) {
    ajaxRequest("GET", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                $("#modal-title").text("Editar denuncia");
                $("#titulo").val(data.body.titulo);
                $("#descripcion").val(data.body.descripcion);
                $("#ubicacion").val(data.body.ubicacion);
                $("#estado").val(data.body.estado);
                $("#ciudadano").val(data.body.ciudadano);
                $("#telefono").val(data.body.telefonoCiudadano);
                $("#guardar").data("id", data.body.id).data("bandera", 0);
                $("#modal-update").modal("show");
            } else {
                showError(data.message);
            }
        })
        .fail(handleError);
}

function clear() {
    $("#modal-title").text("Nueva denuncia");
    $("#titulo").val("");
    $("#descripcion").val("");
    $("#ubicacion").val("");
    $("#estado").val("pendiente"); // Valor predeterminado
    $("#ciudadano").val("");
    $("#telefono").val("");
    $("#guardar").data("id", 0).data("bandera", 1);
}

function handleError(jqXHR) {
    const errorMessage = jqXHR.responseJSON?.message || `Error inesperado. Código: ${jqXHR.status}`;
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
        confirmButtonText: 'Aceptar'
    });
}

$(document).ready(function () {
    $("#tablaDenuncias").DataTable({
        language: {
            lengthMenu: "Mostrar _MENU_ registros",
            zeroRecords: "No se encontraron coincidencias",
            info: "Mostrando del _START_ al _END_ de _TOTAL_ registros",
            infoEmpty: "Sin resultados",
            search: "Buscar: ",
            paginate: {
                first: "Primero",
                last: "Último",
                next: "Siguiente",
                previous: "Anterior",
            },
        },
        columnDefs: [
            { targets: 0, orderable: false}
        ],
    });

    clear();

    $("#nuevo").click(clear);
    
    $("#guardar").click(() => save($("#guardar").data("bandera")));

    $(document).on('click', '.eliminar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        Swal.fire({
            title: 'Eliminar denuncia',
            text: "¿Está seguro de querer eliminar esta denuncia?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Si'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteFila(id);
            }
        });
    });

    $(document).on('click', '.editar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        getFila(id);
    });

    getTabla();

    $('#liAlmacen').addClass("menu-open");
    $('#liDenuncias').addClass("active");
});