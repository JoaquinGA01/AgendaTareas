let btnAgregar = document.getElementById("btnAgregar");
btnAgregar.addEventListener("click", function () {
    var formData = new FormData();
    let NombreT = document.getElementById("NombreTarea").value;
    console.log(NombreT);
    formData.append("NombreTarea", NombreT);
    if (document.getElementById("demo-priority-high").checked) {
        formData.append("Prioridad", "Alta");
    }
    if (document.getElementById("demo-priority-normal").checked) {
        formData.append("Prioridad", "Media");
    }
    if (document.getElementById("demo-priority-low").checked) {
        formData.append("Prioridad", "Baja");
    }
    let Descripcion = document.getElementById("DescripcionTarea").value;
    formData.append("Descripcion", Descripcion);
    let documente = document.querySelector("#ImagenTarea");
    formData.append("Imagen", documente.files[0]);
    let Correo = document.getElementById("Correo").value;
    formData.append("Correo", Correo);
    let Pass = document.getElementById("Pass").value;
    formData.append("Pass", Pass);
    axios({
        method: 'post',
        url: '/GuardarTarea',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
    })
        .then(function (rs) {
            console.log(rs.data);
            sleep(1000);
            window.location.reload();
        })
        .catch(function (error) {
            console.log(error);
        });
}, true)

function sleep(milliseconds) {
    var start = new Date().getTime();
    for (var i = 0; i < 1e7; i++) {
     if ((new Date().getTime() - start) > milliseconds) {
      break;
     }
    }
   }