const btnAlterateName = document.querySelector(
  ".username_container_perfil span"
);
const btnAlterateImage = document.querySelector("#alterate_image");
const btnAlteratePass = document.querySelector(".alterate_password");
const deleteAccount = document.querySelector(".delete_account");
const popUpPerfil = document.querySelector(".pop_up_perfil");
const formName = document.querySelector("#perfil_name_form");
const formPass = document.querySelector("#perfil_password_form");
const formDel = document.querySelector("#perfil_delete_form");
const closePopUpName = document.querySelector("#close_name");
const closePopUpPass = document.querySelector("#close_pass");
const closePopUpDel = document.querySelector("#close_del");
const mainPerfil = document.getElementsByTagName("main");
const toDo = document.querySelector("#to-do");
const minhasListas = document.querySelector("#minhas-listas");

btnAlterateName.addEventListener("click", () => {
  visibilityScreen(popUpPerfil);
  visibilityScreen(formName);
});

btnAlteratePass.addEventListener("click", () => {
  visibilityScreen(popUpPerfil);
  visibilityScreen(formPass);
});

deleteAccount.addEventListener("click", () => {
  visibilityScreen(popUpPerfil);
  visibilityScreen(formDel);
});

closePopUpName.addEventListener("click", () => {
  switchScreens(popUpPerfil, formName);
  switchScreens(main, popUpPerfil);
});

closePopUpPass.addEventListener("click", () => {
  switchScreens(popUpPerfil, formPass);
  switchScreens(main, popUpPerfil);
});

closePopUpDel.addEventListener("click", () => {
  switchScreens(popUpPerfil, formDel);
  switchScreens(main, popUpPerfil);
});

minhasListas.addEventListener("click", () => {
  minhasListas.submit();
});

toDo.addEventListener("click", () => {
  toDo.submit();
})

btnAlterateImage.addEventListener("change", (event) => {
  event.preventDefault();
  var userId = document.getElementById('user-id').elements.id.value;

  var xhr = new XMLHttpRequest();
  xhr.open('POST', 'http://localhost:6789/get-user', true);
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  xhr.send('id=' + userId);

  xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
        const imageForm = document.getElementById("image_form");
        imageForm.submit();
      } else {
        alert('Usuario não identificado');
      }
    }
  };
});