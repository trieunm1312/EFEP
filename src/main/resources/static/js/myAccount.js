//Change avatar
let profileAvatar = document.getElementById("profile-avatar");
let inputFile = document.getElementById("input-file");

inputFile.onchange = function(){
    profileAvatar.src = URL.createObjectURL(inputFile.files[0]);
}

let response = {
    avatar: "/path/to/saved/avatar.png"  // This should be the path returned by the server
};

// Update the avatar src after saving
profileAvatar.src = response.avatar;