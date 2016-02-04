appStore.config(['$routeProvider', '$httpProvider', '$locationProvider',
    function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: '/pages/landing.html'
            })
            .when('/login', {
                templateUrl: '/pages/security/login.html',
                controller: 'CustomerController'
            })
            .when('/register', {
                templateUrl: '/pages/registration.html',
                controller: 'CustomerController'
            })
            .when('/description', {
                templateUrl: '/pages/selected_application.html',
                controller: 'ApplicationController'
            })

            .when('/app_store', {
                templateUrl: '/pages/main_application_store.html',
                controller: 'ApplicationController'
            })
            .when('/upload_application', {
                templateUrl: '/pages/upload_application.html',
                controller: 'ApplicationController'
            })
            .when('/view_applications', {
                templateUrl: '/pages/developed_applications.html',
                controller: 'ApplicationController'
            })
    }
]);