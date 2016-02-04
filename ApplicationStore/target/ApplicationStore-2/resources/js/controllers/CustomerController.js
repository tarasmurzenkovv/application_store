appStore.controller('CustomerController',
    ['$rootScope', '$scope', '$http', '$window', 'CustomerInstance', 'CustomerService',
        function ($rootScope, $scope, $http, $window, customerInformation, CustomerService) {
            angular.element(document).ready(function () {
                $scope.newCustomerInformation = customerInformation.getInitializedCustomerInformation();
                CustomerService.getCustomerTypes($http)
                    .success(function (data) {
                        $scope.customerTypes = data;
                    })
                    .error(function (data) {
                        displayErrorMessages(data)
                    });
            });

            $rootScope.Login = function () {
                $scope.errorMessage = null;
                CustomerService.login($scope.username, $scope.password, $scope);
            };

            $scope.ProcessRegistration = function () {
                CustomerService.processRegistration($http, $scope.newCustomerInformation)
                    .success(function () {
                        customerInformation.setCustomerType($scope.newCustomerInformation.customerType.customerTypeName);
                        customerInformation.setCustomerLogin($scope.login);
                        $rootScope.customerType = $scope.newCustomerInformation.customerType.customerTypeName;
                        if ($scope.newCustomerInformation.customerType.customerTypeName === "DEVELOPER") {
                            $window.location.href = "http://" + $window.location.host + '/pages/index.html#/upload_application';
                        }
                        else if ($scope.newCustomerInformation.customerType.customerTypeName === "REGULAR") {
                            $window.location.href = "http://" + $window.location.host + '/pages/index.html#/app_store';
                        }
                    })
                    .error(function (data) {
                        $window.location.href = "http://" + $window.location.host + '/pages/index.html#/register';
                        displayErrorMessages(data);
                    });
            };
        }]);
