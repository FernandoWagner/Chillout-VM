btnCadastroList.addEventListener("click", () => {
  visibilityScreen(cadastroListScreen);
});

btnCadastroTask.addEventListener("click", () => {
  visibilityScreen(taskListScreen);
});

btnCloseCadastroList.addEventListener("click", () => {
  switchScreens(main, cadastroListScreen);
});

btnCloseCadastroTask.addEventListener("click", () => {
  switchScreens(main, taskListScreen);
});
