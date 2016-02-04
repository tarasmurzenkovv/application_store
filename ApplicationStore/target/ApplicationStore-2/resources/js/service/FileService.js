appStore.service('FileService', ['$http', '$window', function ($http, $window) {
    var applicationMetaData = {
        applicationName: '',
        applicationDescription: '',
        categoryName: '',
        file: null
    };

    this.initApplicationMetaData = function () {
        return applicationMetaData;
    };

    this.uploadFileToUrl = function (applicationMetaData, file, uploadUrl) {
        var fd = new FormData();
        fd.append('applicationName', applicationMetaData.applicationName);
        fd.append('applicationDescription', applicationMetaData.applicationDescription);
        fd.append('applicationCategory', applicationMetaData.categoryName.categoryName);
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function () {
                $window.location.href = "http://" + $window.location.host + '/pages/index.html#/view_applications';
            })
            .error(function (error) {
                displayErrorMessages(error);
            });
    };

    this.downloadFile = function (applicationName) {
        $http({
            method: 'POST',
            url: '/download_app?name=' + applicationName,
            responseType: 'arraybuffer'
        })
            .success(function (data) {
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.style = "display: none";
                var blob = new Blob([data], {type: 'application/zip'});
                var url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = applicationName;
                a.click();
                window.URL.revokeObjectURL(url);
            })
            .error(function (response) {
                displayErrorMessages(response);
            })
    };
}]);
