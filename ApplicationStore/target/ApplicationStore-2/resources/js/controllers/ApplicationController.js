appStore.controller('ApplicationController',
    ['$scope', '$http', 'ApplicationService', 'FileService', 'ApplicationInstance', 'CustomerService',
        function ($scope, $http, ApplicationService, FileService, ApplicationInstance, CustomerService) {
            $scope.theMostPopularApplications = null;
            $scope.applicationCategories = null;
            $scope.selectedCategory = null;
            $scope.applicationsPerCategory = null;
            $scope.applicationDescription = null;

            $scope.theMostPopularApplications = ApplicationInstance.getTheMostInterestingApplications();
            $scope.selectedApplicationInstance = ApplicationInstance.getSelectedApplicationInstance();


            angular.element(document).ready(function () {
                ApplicationService.getTheMostPopularApplications()
                    .success(function (data) {
                        $scope.theMostPopularApplications = data;
                    })
                    .error(function (error) {
                        displayErrorMessages(error);
                    });
                ApplicationService.getApplicationCategories()
                    .success(function (data) {
                        $scope.applicationCategories = data;
                        $scope.selectedCategory = data[0].categoryName;
                        ApplicationService.getAppsForCategory($scope.selectedCategory)
                            .success(function (data) {
                                $scope.applicationsPerCategory = data;
                            })
                            .error(function (error) {
                                displayErrorMessages(error);
                            })
                    })
                    .error(function (error) {
                        displayErrorMessages(error);
                    });
                CustomerService.getCustomerUploadedApplications($http)
                    .success(function (data) {
                        $scope.customerApplications = data;
                    })
                    .error(function (response) {
                        displayErrorMessages(response);
                    })

            });
            $scope.downLoadApp = function (appName) {
                FileService.downloadFile(appName);
            };
            $scope.setSelectedApplication = function (application) {
                $scope.selectedApplicationInstance = application;
            }

            $scope.setApplicationInstance = function (applicationInstance) {
                ApplicationInstance.setTheMostInterestingApplications($scope.theMostPopularApplications);
                ApplicationInstance.setSelectedApplicationInstance(applicationInstance);
            };

            $scope.getAppsForCategory = function (categoryName) {
                ApplicationService.getAppsForCategory(categoryName)
                    .success(function (data) {
                        $scope.selectedCategory = categoryName;
                        $scope.applicationsPerCategory = data;
                    })
                    .error(function (error) {
                        displayErrorMessages(error);
                    })
            };
            $scope.UploadFile = function () {
                FileService.uploadFileToUrl($scope.applicationDescription, $scope.myFile, '/upload_application');
            };
        }]);