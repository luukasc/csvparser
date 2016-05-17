function SearchController($scope, $http) {
    $scope.results = [];
    $scope.input = "";
    $scope.doSearch = function() {
        var httpRequest = $http({
            method : 'GET',
            url : "/search/" + $scope.input,
        }).success(function(data, status) {
            $scope.results = data;
        }).error(function(arg) {
            alert("error ");
        });  
    };
    
    // run the search when the page loads.
    $scope.doSearch();
}

function PageController($scope, $http, $window) {
	// $scope.results = [];
	$scope.passResult = function(arg) {
        	$window.location.href = "files/detail/" + arg;
    };
}

function FlowController($scope, $http, $window) { 
	$scope.backToLogin = function() {
        	$window.location.href = "/logout";
    },
    $scope.backToFiles = function() {
    	$window.location.href = "/files";
    },
	$scope.createUser = function() {
    	$window.location.href = "/createUser";
    };
}

function SomeController($scope,  $http) {
	$scope.customHtml = "";
	var httpRequest = $http({
        method : 'GET',
        url : "/createUser",
    }).success(function(data, status) {
    	$scope.customHtml = data;
    }).error(function(arg) {
    	$scope.customHtml = "Error";
    });

}
