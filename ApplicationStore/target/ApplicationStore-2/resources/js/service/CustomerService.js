appStore.service('CustomerService', ['$http', '$cookies', '$window', function ($http, $cookies, $window) {
    this.getCustomerTypes = function ($http) {
        return $http({
            method: 'get',
            url: '/customer_types'
        })
    };
    this.processRegistration = function ($http, customerData) {
        return $http({
            method: 'POST',
            url: '/register',
            data: customerData
        })
    };
    this.getCustomerUploadedApplications = function ($http) {
        return $http({
            method: 'get',
            url: '/customer_applications'
        })
    };
    this.login = function (login, password, $scope) {
        $http.defaults.headers.common['Authorization'] = 'Basic ' + btoa(login + ':' + password);
        $http({
            method: 'POST',
            url: '/ajax_login'
        })
            .success(function () {
                var customerType = $cookies.get('ApplicationStore');
                if (customerType === 'DEVELOPER') {
                    $window.location.href = "http://" + $window.location.host + '/pages/index.html#/upload_application';
                } else {
                    $window.location.href = "http://" + $window.location.host + '/pages/index.html#/app_store';
                }
            })
            .error(function (response) {
                $scope.errorMessage = response;
            });
    };
}]);