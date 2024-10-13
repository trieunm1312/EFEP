//Change avatar
let profileAvatar = document.getElementById("profile-avatar");
let inputFile = document.getElementById("input-file");

inputFile.onchange = function(){
    profileAvatar.src = URL.createObjectURL(inputFile.files[0]);
}

//Save URL to local storage
document.querySelector("#input-file").addEventListener("change", function(){
    const reader = new FileReader();

    reader.addEventListener("load", () => {
        localStorage.setItem("recent-image", reader.result);
    });

    reader.readAsDataURL(this.files[0]);
});

document.addEventListener("DOMContentLoaded", () => {
    const recentImageDataUrl = localStorage.getItem("recent-image");

    if(recentImageDataUrl){
        document.querySelector("#profile-avatar").setAttribute("src", recentImageDataUrl);
    }
});