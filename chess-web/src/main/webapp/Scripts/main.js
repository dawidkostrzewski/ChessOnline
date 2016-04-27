$(document).ready(function(){
   setModule = function(){
       $('li').removeClass('active');
       if(sessionStorage.getItem('actualModule')){
           $('#'+ sessionStorage.getItem('actualModule')).addClass('active');
       }
       else{
           $('#Home').addClass('active');
       }
   };
    $("body").click(function(){
        $('#Home').removeClass("animated infinite bounceIn active");
    });
    init = function(){
        var user = JSON.parse(sessionStorage.getItem('actualUser'));
        var _cometd = $.cometd,
            _connected = false, _handshaked = false;
        var url = "http://localhost:8080/ChessRests/sync/cometd";
        _cometd.configure({
            url : url,
            logLevel : 'warning'
        });
        _cometd.addListener('/meta/handshake', _metaHandshake);
        _cometd.addListener('/meta/connect', _metaConnect);

        _cometd.handshake();
        function _metaConnect(message)
        {
            if (_cometd.isDisconnected()) {
                console.log('_metaConnect isDisconnected');
                _connected = false;
                return;
            }

            var wasConnected = _connected;
            _connected = message.successful === true;

            if (!wasConnected && _connected)
            {
                console.log(user);
                var channel = "/ChessRests/sync/invites/" + user.id;
                console.log(channel);
                _cometd.subscribe(channel, function(){
                    console.log("Subscribed Invites Channel");
                    $('#Home').addClass("animated infinite bounceIn active");
                });
                console.log('_metaConnect _connected')
            }
            else if (wasConnected && !_connected)
            {
                console.log('_metaConnect _connectionError')
            }
        }

        function _metaHandshake(handshake)
        {
            if (handshake.successful === true) {
                _handshaked = true;
                console.log('handshake successful')
            }
        }
        setModule();
    };

    var globalContext = global.context();
    toastr.options = globalContext.messageConfig();
    var app = angular.module('navApp',['ngRoute']);
    app.config(function($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: "Scripts/Templates/index.html",
                controller: "mainController"
            })
            .when('/Games',{
                templateUrl: "Scripts/Templates/games.html",
                controller: "gamesController"
            })
            .when('/Ranking',{
                templateUrl: "Scripts/Templates/ranking.html",
                controller: "rankingController"
            })
            .when('/Profile/:id',{
                templateUrl: "Scripts/Templates/profile.html",
                controller: "profileController"
            })
    });

    /***********MAIN MODULE CONTROLLER***********/

    app.controller('mainController', function($scope, $http, sharedProperties){
        $scope.actualUser = sharedProperties.getActualUser();
        $scope.Invites = [];
        $scope.Users = [];
        $scope.Messages = [];
        var deletedMessage;
        $scope.init = function(){
            if(sharedProperties.getActualUser() != null){
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/users"},function(response){
                    $scope.Users = response.data;
                });
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/invites/" + $scope.actualUser.id},function(response){
                    $scope.Invites = response.data;
                });
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/messages/" + $scope.actualUser.id},function(response){
                    $scope.Messages = response.data;
                });
            } else{
                window.location.href = "http://localhost:8080/ChessOnline/";
            }

        };

        $scope.showDate = function(date){
            var myDate = new Date(date);
            return myDate.getDate() + "." + (myDate.getMonth() + 1) + "." + myDate.getFullYear() + " " + myDate.toLocaleTimeString();
        };
        $scope.getSenderLogin = function(id){
            if(id == null){
                return "ChessOnline System"
            }
            for (var i=0; i<$scope.Users.length; i++){
                if($scope.Users[i].id === id){
                    return $scope.Users[i].login;
                }
            }
            return "USER NOT FOUND";
        };
        $scope.acceptInvite = function(invite){
            var message = null;
            var newGame = {
                fen: globalContext.startFEN(),
                opponentId: invite.senderId,
                userId: $scope.actualUser.id,
                date: new Date().getTime(),
                finished: false
            };

            sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/games/", data: newGame},function(response){
                invite.gameId = response.data.id;
                invite.playable = false;
                message = {
                    text: globalContext.acceptInviteText($scope.actualUser.login,response.data.id),
                    creationtime: new  Date().getTime(),
                    senderId: null,
                    reciverId: invite.senderId,
                    type: globalContext.messageTypes().system
                };
            });

            setTimeout(function(){
                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/invites", data: invite},function(response){
                    console.log(response);
                    toastr.success("Accepted " + $scope.getSenderLogin(invite.senderId) + " invite.")
                });
            },200);

            setTimeout(function(){
                sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages/", data: message},function(response){
                    console.log(response);
                });
            },300);


        };
        $scope.removeMessage = function(message){
            $('#myModal').modal('show');
            deletedMessage = message;
        };
        $scope.confirmRemove = function(){
            $http.delete(globalContext.mainURL() + "/messages/" + deletedMessage.id)
                .then(function(response){
                    toastr.success("Removed message.");
                    $('#myModal').modal('hide');
                    $scope.Messages.splice($scope.Messages.indexOf(deletedMessage),1);
                }
                ,function(){
                    toastr.error("Error! Can't delete message.");
                    $('#myModal').modal('hide');
                })
        }
    });

    /***********NAVIGATION MODULE CONTROLLER***********/

    app.controller('navController', function($scope, sharedProperties){
        $scope.actualUser = sharedProperties.getActualUser();
        $scope.setActiveModule = function(el){
            $('li').removeClass('active');
            $('#'+el).addClass('active');
            sessionStorage.setItem('actualModule',el.toString());
            if(el === "Profile"){
                sharedProperties.setShowUser(JSON.parse(sessionStorage.getItem('actualUser')));
            }
        };
        $scope.logOut = function(){
            sessionStorage.clear();
            sharedProperties.setShowUser(null);
            sharedProperties.clearActualUser();
            sharedProperties.setUsers(null);
            window.location.href = "http://localhost:8080/ChessOnline/";

        }
    });

    /***********GAME MODULE CONTROLLER***********/

    app.controller('gamesController', function($scope,$http,sharedProperties){
        $scope.Games = [];
        $scope.actualUser = sharedProperties.getActualUser();
        $scope.Users = [];
        $scope.User = {};
        $scope.Game = {};
        $scope.init = function(){
            if(sharedProperties.getActualUser() != null){
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/users"}, function(response){
                    $scope.Users = response.data;
                    sharedProperties.setUsers(response.data);
                });
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/games/" + $scope.actualUser.id}, function(response){
                    console.log(response);
                    $scope.Games = response.data;
                });
            } else {
                window.location.href = "http://localhost:8080/ChessOnline/";
            }

        };

        $scope.showOpponent = function(game){
            var Users = sharedProperties.getUsers();
            if(game.opponentId == $scope.actualUser.id){
                for(var i=0;i<Users.length;i++){
                    if(Users[i].id == game.userId){
                        return Users[i].login;
                    }
                }
            } else {
                for(i=0;i<Users.length;i++){
                    if(Users[i].id == game.opponentId){
                        return Users[i].login;
                    }
                }
            }
            return "USER NOT FOUND";
        };

        $scope.showDate = function(date){
            var myDate = new Date(date);
            return myDate.getDate() + "." + (myDate.getMonth() + 1) + "." + myDate.getFullYear();
        };

        $scope.startSelectedGame = function(game){
            var index = 0;
            for( var i=0; i<$scope.Games.length; i++){
                if (game.id === $scope.Games[i].id){
                    index = i;
                }
            }
            var myGame = new Chess();
            myGame.load($scope.Games[index].fen);;
            $scope.handleMove = function(source, target) {

                var move = myGame.move({
                    from: source,
                    to: target,
                    promotion: 'q'
                });

                if (move) {
                    if(myGame.game_over()){
                        alert('Koniec gry');
                        return "snapback";
                    } else {
                        $scope.Games[index].fen = myGame.fen();
                        sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/games/", data: $scope.Games[index]},function(response){
                            console.log(response);
                        });
                    }
                } else {
                    return 'snapback';
                }
                return null;

            };
            function onDragStart(){
                if(myGame.turn() == 'w' && $scope.actualUser.id != game.userId ||
                    myGame.turn() == 'b' && $scope.actualUser.id != game.opponentId) {
                    return false;
                }
                return true;
            }
            var orientation = "";
            if ($scope.actualUser.id == game.userId){
                orientation = "white";
            }
            else if($scope.actualUser.id == game.opponentId){
                orientation = "black"
            }
            var cfg = {
                draggable: true,
                onDrop: $scope.handleMove,
                position: $scope.Games[index].fen,
                onDragStart: onDragStart,
                orientation: orientation
            };
           var board = new ChessBoard('board', cfg);
        };

        $scope.showMove = function(game){
                var myGame = new Chess();
                myGame.load(game.fen);
                if (myGame.turn() == 'w' && $scope.actualUser.id === game.userId ||
                    myGame.turn() == 'b' && $scope.actualUser.id === game.opponentId) {
                    return "Your move";
                } else {
                    return "Opponent move";
                }
        };
    });

    /***********RANKING MODULE CONTROLLER***********/

    app.controller('rankingController', function($scope, $http, sharedProperties){
        $scope.Users = [];
        $scope.init = function(){
            if(sharedProperties.getActualUser() != null){
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + '/users'}, function(response){
                    console.log(response);
                    $scope.Users = response.data;
                    sharedProperties.setUsers(response.data);
                });
            } else {
                window.location.href = "http://localhost:8080/ChessOnline/";
            }
        };
        $scope.showUserProfile = function(user,position){
            sharedProperties.setPosition(position);
            sharedProperties.setShowUser(user);
            sessionStorage.setItem('showUser',JSON.stringify(user));
            window.location.replace('#Profile/' + user.id);
        }

    });

    /***********PROFILE MODULE CONTROLLER***********/

    app.controller('profileController',function($scope, $http, sharedProperties){
        $scope.actualUser = null;
        $scope.showUser = null;
        $scope.position = null;
        $scope.Games = [];
        $scope.Users = [];
        $scope.showMessage = false;
        $scope.init = function(){
            if(sharedProperties.getActualUser() != null){
                $scope.actualUser = sharedProperties.getActualUser();
                $scope.showUser = sharedProperties.getShowUser();
                $scope.position = sharedProperties.getPosition();
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/games/" + $scope.showUser.id}, function(response){
                    $scope.Games = response.data;
                });
                $scope.Users = sharedProperties.getUsers();
                if ($scope.Users.length === 0){
                    sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/users/"}, function(response){
                        $scope.Users = response.data;
                    });
                }
            } else {
                window.location.href = "http://localhost:8080/ChessOnline/";
            }

        };
        $scope.showPosition = function(id){
            for(var i=0;i<$scope.Users.length;i++){
                if($scope.Users[i].id == id){
                    return i+1;
                }
            }
            return 0;
        };
        $scope.showOpponent = function(game){
            if(game.opponentId == $scope.showUser.id){
                for(var i=0;i<$scope.Users.length;i++){
                    if($scope.Users[i].id == game.userId){
                        return $scope.Users[i].login;
                    }
                }
            } else {
                for(i=0;i<$scope.Users.length;i++){
                    if($scope.Users[i].id == game.opponentId){
                        return $scope.Users[i].login;
                    }
                }
            }
            return "USER NOT FOUND";
        };
        $scope.sendInvite = function(user){
            var date =  new Date().getTime();
            var invite = {
                creationtime: date,
                reciverId: user.id,
                senderId: $scope.actualUser.id,
                playable: true,
                gameId: null
            };
            sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() +  '/invites', data: invite}, function(){
                toastr.success("Sent invite to " + user.login);
            });

        };
        $scope.changeShowMessage = function(){
            $scope.showMessage = !$scope.showMessage;
            if(!$scope.showMessage){
                $scope.messageText = "";
            }
        };
        $scope.sendUserMessage = function(user){
            var message = {
                creationtime: new Date().getTime(),
                reciverId: user.id,
                senderId: $scope.actualUser.id,
                text: $('#messageText').val(),
                type: globalContext.messageTypes().users
            };
            sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages/", data: message}, function(){
                toastr.success("Sent message to " + user.login);
                $scope.changeShowMessage();
            });
        }
    });
    app.service('sharedProperties', function($http) {
        var actualUser = JSON.parse(sessionStorage.getItem('actualUser'));
        var showUser = {};
        var showUserPosition = 0;
        var Users = [];
        return {
            getActualUser: function() {
                return actualUser;
            },
            clearActualUser: function(){
                actualUser = null;
            },
            setShowUser: function(user){
                showUser = user;
            },
            getShowUser: function(){
                console.log(showUser);
                return showUser;
            },
            setPosition: function(position){
                showUserPosition = position;
            },
            getPosition: function(){
                return showUserPosition;
            },
            setUsers: function(users){
                Users = users;
            },
            getUsers: function(){
                return Users;
            },
            getData: function(config,callback){
                setTimeout(function(){
                    $http(config).then(function(response){
                        if(callback){
                            callback(response);
                        }
                    }, function(){
                        toastr.error('GET DATA ERROR.');
                    })

                },200)
            },
            sendData: function(config,callback) {
                setTimeout(function(){
                    $http(config).then(function(response){
                        if(callback){
                            callback(response);
                        }
                    }, function(){
                        toastr.error("SEND DATA ERROR.");
                    })
                },200);
            }
        }
    });
    angular.bootstrap(document, ['navApp']);
});
