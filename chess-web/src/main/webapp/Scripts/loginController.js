$(document).ready(function(){
    var globalContext = global.context();
    toastr.options = globalContext.messageConfig();
    var app = angular.module("myApp",['ngRoute']);
    app.config(function($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: "Scripts/Templates/login.html",
                controller: "loginController"
            })
            .when('/Registry',{
                templateUrl: "Scripts/Templates/registry.html",
                controller: "registryController"
            })
    });
    app.controller("loginController",function($scope,$http){
        $scope.logIn = function(){
            if($scope.login != "" && $scope.pass != ""){
                $http.get(globalContext.mainURL() + "/users/login/" + $scope.login)
                    .then(function(response){
                        console.log(response);
                        if(response.data && response.data != ""){
                            if(response.data.password == $scope.pass){
                                console.log("Zalogowano");
                                sessionStorage.setItem('actualUser',JSON.stringify(response.data));
                                console.log(sessionStorage.getItem('actualUser'));
                                window.location.replace("main.html");
                            }
                            else{
                                toastr.error("Wrong password! Please try again.")
                            }
                        }
                        else if(response.data == null || response.data === ""){
                            toastr.error("User with this login don't exist in database.")
                        }
                    })
            }
            else if($scope.login == ""){
                toastr.error("Please enter login.")
            }
            else{
                toastr.error("Please enter password.");
            }
        }
    });

    app.controller("registryController",function($scope,$http){
        $scope.registryNewUser = function(){
            var newUser = {
                login: $scope.login,
                password: $scope.pass,
                firstname: $scope.firstName,
                lastname: $scope.lastName,
                email: $scope.email,
                wins: 0,
                ties: 0,
                loses: 0,
                points: 0
            };
            if( $scope.pass == $scope.pass2){
                $http.post(globalContext.mainURL() + "/users/",newUser)
                    .success(function(response){
                        sessionStorage.setItem('actualUser',JSON.stringify(response));
                        console.log(sessionStorage.getItem('actualUser'));
                        window.location.replace("main.html");
                    }).error(function(){
                        toastr.error("User with this login already exist in database.")
                    });
            }
            else{
                toastr.error("Error with passwords.")
            }
        }
    });
    angular.bootstrap(document, ['myApp']);
});