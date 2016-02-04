appStore.factory('CustomerInstance', function () {
    var newCustomerInformation = {
        firstName: '',
        lastName: '',
        dateOfBirth: '',
        login: '',
        password: '',
        email: '',
        customerType:{
            id:'',
            customerTypeName:''
        }
    };
    var customerType;
    var customerLogin;
    return {
        setCustomerLogin: function (login) {
            customerLogin = login
        },
        setCustomerType: function (type) {
            customerType = type;
        },
        getInitializedCustomerInformation: function(){
            return newCustomerInformation;
        }
    };
});