var errorLabels =
    ["globalServerError", "firstNameError", "lastNameError", "loginError", "passwordError", "emailError",
    "customerTypeError", "dateOfBirthError", "nameError", "addressError", "dateError", "priceError"];

function displayErrorMessages(data) {
    var errors = data;
    errorLabels.forEach(function(errorLabel) {
        var element = document.getElementById(errorLabel);
        if (element !== null) {
            if (errors.hasOwnProperty(errorLabel)) {
                element.innerHTML = errors[errorLabel];
            } else {
                element.innerHTML = '';
            }
        }
    })
}
