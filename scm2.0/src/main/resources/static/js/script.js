// <<<<change page theme work>>>>

//set theme to local storage
function setTheme(theme){
    localStorage.setItem("theme", theme);
}
//get theme to local storage 
function getTheme(){
    let theme = localStorage.getItem("theme");
    if(theme) return theme;
    else return "light";
    //return theme ? theme : "light";
}

let currenttheme = getTheme();

//Initially >>>> apne aap chalega

document.addEventListener('DOMContentLoaded', (event) => {
    //change the theme of the page
    changeTheme();
});


function changeTheme(){

 changePageTheme(currenttheme, "");

//set thelistener for the theme change button
const changeThemeButton = document.querySelector('#themeToggle');

changeThemeButton.addEventListener('click', (event) =>{
    //remove the current theme
    //document.querySelector('html').classList.remove(currenttheme); >>> or
    const oldTheme = currenttheme;

    if(currenttheme == "dark"){
        //change to light
        currenttheme = "light";
    }
    else{
        //change to dark
        currenttheme = "dark";
    }
   changePageTheme(currenttheme,oldTheme);
});    
}


//change current page theme 

    function changePageTheme(theme,oldTheme){
    //local storage me update krna hai 
    setTheme(theme);
    //remove the old theme
   if(oldTheme){
    document.querySelector('html').classList.remove(oldTheme);
   }
    //add the new theme
    document.querySelector('html').classList.add(theme);

    //change the text of button
    document.querySelector('#themeToggle').
    querySelector('span').textContent = theme =="light"? "Dark" : "Light";
}
// >>>> Change Page Theme End Here <<<<