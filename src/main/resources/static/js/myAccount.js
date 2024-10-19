let profileAvatar = document.getElementById("profile-avatar");
let inputFile = document.getElementById("input-file");

inputFile.onchange = async function () {
    const file = inputFile.files[0];
    if (file) {
        // Use Firebase Storage ref to define the storage path
        const storageRef = ref(storage, `images/avatars/${file.name}`);

        try {
            // Upload the file
            const snapshot = await uploadBytes(storageRef, file);

            // Get the download URL
            const downloadURL = await getDownloadURL(snapshot.ref);

            // Update the image source to display the uploaded image
            profileAvatar.src = downloadURL;

            // Store the download URL in a hidden input field to send to the server
            document.querySelector("input[name='avatarUrl']").value = downloadURL;

            console.log("File available at: ", downloadURL);

        } catch (error) {
            console.error("Error uploading file: ", error);
        }
    }
};
