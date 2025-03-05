console.log("script loaded");
let currentTheme = getTheme();
//intially
document.addEventListener('DOMContentLoaded',()=>{
    changeTheme();
})

//TODO
function changeTheme() {
  //set to webpage
  changePagetheme(currentTheme, currentTheme);
  //set the listener to change theme button
  const changeThemeButton = document.querySelector("#theme_change_button");
  //change the text of button
 
 
  changeThemeButton.addEventListener("click", (event) => {
let oldTheme = currentTheme;
    console.log("Change theme button clicked");
    
    if (currentTheme === "dark") {
      //theme to light
      currentTheme = "light";
    } else {
      currentTheme = "dark";
    }
    changePagetheme(currentTheme, oldTheme);
  });
}

//set theme to localstorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

//Get theme from localstorage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}
// change current page theme
function changePagetheme(theme, oldTheme) {
  //local storage update
  setTheme(currentTheme);
  //remove current theme
  document.querySelector("html").classList.remove(oldTheme);
  //set the current theme
  document.querySelector("html").classList.add(theme);
  //change the text of button
  document
    .querySelector("#theme_change_button")
    .querySelector("span").textContent = theme === "light" ? "Dark" : "Light";
}
