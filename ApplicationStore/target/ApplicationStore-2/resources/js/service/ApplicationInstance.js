appStore.factory('ApplicationInstance', [function () {
    var applicationInstance = null;
    var theMostInterestingApplications = null;

    return{

        getTheMostInterestingApplications:function(){
            return theMostInterestingApplications;
        },
        setTheMostInterestingApplications: function (givenTheMostInterestingApplications) {
            theMostInterestingApplications = givenTheMostInterestingApplications;
        },
        getSelectedApplicationInstance:function(){
            return applicationInstance;
        },
        setSelectedApplicationInstance: function (selectedApplicationInstance) {
            applicationInstance = selectedApplicationInstance;
        }
    };
}]);
