appStore.service('ApplicationService', ['$http',function ($http) {
    this.getApplicationCategories = function(){
        return $http({
            method:'get',
            url:'/app_categories'
        })
    };

    this.getTheMostPopularApplications = function(){
        return $http({
            method: 'get',
            url: '/popular_apps'
        })
    };
    this.getAppsForCategory = function(categoryName){
        return $http({
            method: 'get',
            url: '/apps_per_category?name='+categoryName
        })
    };
}]);
