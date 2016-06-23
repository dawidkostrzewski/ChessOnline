$(document).ready(function(){
    $(window).resize(function() {
        $('#messagesDiv').css('max-height', window.innerHeight - 150);
        $('#invitesDiv').css('max-height', window.innerHeight - 150);
        $('#invitesDiv2').css('max-height', window.innerHeight - 150);
        $('#playerGames').css('max-height', window.innerHeight - 150);
        $('#messagesDiv').css('min-height', window.innerHeight - 150);
        $('#playerGamesList').css('max-height', window.innerHeight - 150);
        $('#invitesDiv').css('min-height', window.innerHeight - 150);
        $('#invitesDiv2').css('min-height', window.innerHeight - 150);
        $('#playerGames').css('min-height', window.innerHeight - 150);
        $('#playerGamesList').css('min-height', window.innerHeight - 150);
    });
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
        $('#Home').removeClass("animated infinite bounceIn");
        $('#Games').removeClass("animated infinite bounceIn");
    });
    init = function(){

        var user = JSON.parse(sessionStorage.getItem('actualUser'));
        var _cometd = $.cometd,
            _connected = false, _handshaked = false;
        var url = "http://" + window.location.hostname + "/ChessRests/sync/cometd";
        console.log(url);
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
                var inviteChannel = "/ChessRests/sync/invites/" + user.id;
                var messageChannel = "/ChessRests/sync/messages/" + user.id;
                var gameChannel = "/ChessRests/sync/games/" + user.id;
                console.log(inviteChannel);
                _cometd.subscribe(inviteChannel, function(){
                    console.log("Subscribed Invites Channel");
                    $('#Home').addClass("animated infinite bounceIn active");
                });
                _cometd.subscribe(messageChannel, function(){
                    console.log("Subscribed Message Channel");
                    $('#Home').addClass("animated infinite bounceIn active");
                });
                _cometd.subscribe(gameChannel, function(){
                    console.log("Subscribed Games Channel");
                    $('#Games').addClass("animated infinite bounceIn active");
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
            .when('/Settings',{
                templateUrl: "Scripts/Templates/settings.html",
                controller: "settingsController"
            })
    });

    /***********MAIN MODULE CONTROLLER***********/

    app.controller('mainController', function($scope, $http, sharedProperties){
        $scope.actualUser = sharedProperties.getActualUser();
        $scope.Invites = [];
        $scope.Users = [];
        $scope.Messages = [];
        $scope.Friends = [];
        var deletedMessage;
        $scope.init = function(){
            $('#messagesDiv').css('max-height',window.innerHeight - 150);
            $('#invitesDiv').css('max-height', window.innerHeight - 150);
            $('#invitesDiv2').css('max-height', window.innerHeight - 150);
            $('#messagesDiv').css('min-height',window.innerHeight - 150);
            $('#invitesDiv').css('min-height', window.innerHeight - 150);
            $('#invitesDiv2').css('min-height', window.innerHeight - 150);
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
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/friends/" + $scope.actualUser.id},function(response){
                    $scope.Friends = response.data;
                });
            } else{
                window.location.href = "http://" + window.location.hostname + "/ChessOnline/";
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
        $scope.checkOnline = function(id){
            for(var i=0;i<$scope.Users.length;i++){
                if($scope.Users[i].id == id){
                    return $scope.Users[i].online;
                }
            }

            return false;
        };
        $scope.changeShowMessage = function(user){
            if(document.getElementById('row_'+user.login.replace(".","_")).style.display == "none"){
                $("#row_" + user.login.replace(".","_")).css("display","block");
            } else {
                $("#row_" + user.login.replace(".","_")).css("display","none");
            }

        };
        $scope.sendUserMessage = function(user){
            var userElement = "Message_" + user.login.replace(".","_");
            console.log($("#"+userElement));
            var message = {
                creationtime: new Date().getTime(),
                reciverId: user.id,
                senderId: $scope.actualUser.id,
                text: $("#" + userElement).val(),
                type: globalContext.messageTypes().users
            };
            sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages/", data: message}, function(){
                toastr.success("Sent message to " + user.login);
                $("#" + userElement).val("");
                $("#row_" + user.login.replace(".","_")).css("display","none");
            });
        };
        $scope.sendInvite = function(user){
            var date =  new Date().getTime();
            var invite = {
                creationtime: date,
                reciverId: user.id,
                senderId: $scope.actualUser.id,
                type: "Game",
                playable: true,
                gameId: null
            };
            sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() +  '/invites', data: invite}, function(){
                toastr.success("Sent invite to " + user.login);
            });
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
                    creationtime: new Date().getTime(),
                    senderId: null,
                    reciverId: invite.senderId,
                    type: globalContext.messageTypes().system
                };
                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/invites", data: invite},function(responseInvite){
                    console.log(responseInvite);
                    toastr.success("Accepted " + $scope.getSenderLogin(invite.senderId) + " invite.");
                    sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages/", data: message},function(responseMessage){
                        console.log(responseMessage);
                    });
                });
            });
        };
        $scope.removeMessage = function(message){
            $('#myModal').modal('show');
            deletedMessage = message;
        };
        $scope.confirmRemove = function(){
            $http.delete(globalContext.mainURL() + "/messages/" + deletedMessage.id)
                .then(function(response){
                    $('#myModal').modal('hide');
                    $scope.Messages.splice($scope.Messages.indexOf(deletedMessage),1);
                }
                ,function(){
                    toastr.error("Error! Can't delete message.");
                    $('#myModal').modal('hide');
                })
        };
        $scope.refuseFriendInvite = function(message){
            $scope.removeMessage(message);
            var refuseMessage = {
                text: $scope.actualUser.login + " refuse your invitation to friend list.",
                creationtime: new Date().getTime(),
                senderId: null,
                reciverId: message.senderId,
                type: globalContext.messageTypes().system
            };
            sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages/", data: refuseMessage});
        };
        $scope.refuseGameInvite = function(invite){
            sharedProperties.sendData({method: 'DELETE', url: globalContext.mainURL() + "/invites/" + invite.id}, function(response){
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/invites/" + $scope.actualUser.id}, function(responseInvite){
                    $scope.Invites = responseInvite.data;
                });
            });
            var refuseMessage = {
                text: $scope.actualUser.login + " refuse your invitation to game.",
                creationtime: new Date().getTime(),
                senderId: null,
                reciverId: invite.senderId,
                type: globalContext.messageTypes().system
            };
            sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages/", data: refuseMessage});
        };
        $scope.acceptFriendInvite = function(message){
            var newFriend = {
                userId: $scope.actualUser.id,
                friendId: message.senderId,
                accepted: true
            };

            sharedProperties.sendData({url: globalContext.mainURL() + "/friends", method: "POST", data: newFriend},
                function(response){
                    toastr.success("Accepted friend invite.");
                    deletedMessage = message;
                    $scope.confirmRemove();
                    sharedProperties.getData({url: globalContext.mainURL() + "/friends/" + $scope.actualUser.id, method: "GET"},function(newData){
                        $scope.Friends = newData.data;
                    });
                });

        };
    });

    /***********NAVIGATION MODULE CONTROLLER***********/

    app.controller('navController', function($scope, $http, $route, sharedProperties){
        $scope.actualUser = sharedProperties.getActualUser();
        $scope.setActiveModule = function(el){
            $('li').removeClass('active');
            $('#'+el).addClass('active');
            console.log(sessionStorage.getItem('actualModule'));
            if(el === sessionStorage.getItem('actualModule'))
            {
                $route.reload();
            }
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
            $scope.actualUser.online = false;
            $scope.actualUser.lastactivity = new Date().getTime();
            $http({method: 'PUT', url: globalContext.mainURL() + "/users", data: $scope.actualUser})
            .then(function(response){
                console.log(response.data);
                window.location.href = "http://" + window.location.hostname + "/ChessOnline/";
            }, function(){
                console.log("ERROR");
            })
        }
    });

    /***********GAME MODULE CONTROLLER***********/

    app.controller('gamesController', function($scope,$http,sharedProperties){
        $scope.Games = [];
        $scope.namedGames = [];
        $scope.actualUser = sharedProperties.getActualUser();
        $scope.Users = [];
        $scope.Friends = [];
        $scope.User = {};
        $scope.Game = {};
        $scope.newGame = false;
        $scope.playerPicker = false;
        $scope.selectedUser = {};
        $scope.computerGame = false;
        $scope.gameSelected = false;
        $scope.init = function(){
            $('#playerGames').css('max-height', window.innerHeight - 150);
            $('#playerGames').css('min-height', window.innerHeight - 150);
            if(sharedProperties.getActualUser() != null){
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/users"}, function(response){
                    $scope.Users = response.data;
                    sharedProperties.setUsers(response.data);
                });
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/games/" + $scope.actualUser.id}, function(response){
                    console.log(response);
                    $scope.Games = response.data;
                    $scope.namedGames = response.data;
                    for(var i=0; i < $scope.namedGames.length; i++){
                        $scope.namedGames[i].opponentName = $scope.showOpponent($scope.namedGames[i]);
                    }

                });
            } else {
                window.location.href = "http://" + window.location.hostname + "/ChessOnline/";
            }

        };

        $scope.createNewGame = function(){
            $scope.newGame = true;
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
        $scope.showPlayerPicker = function(el){
            var element = el.target;
            $(".list-group-item").removeClass("active");
            $(element).closest(".list-group-item").addClass("active");
            sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/friends/" + $scope.actualUser.id}, function(response){
                $scope.Friends = response.data;
            });
            $scope.computerGame = false;
            $scope.playerPicker =  true;
        };

        $scope.start = function(game,el){
            var element = el.target;
            $scope.newGame = false;
            $scope.computerGame = false;
            $scope.gameSelected = true;
            $scope.Game = game;
            $(".list-group-item").removeClass("active");
            $(element).closest(".list-group-item").addClass("active");
            var me = this;
            setTimeout(function(){
                me.startSelectedGame(game);
            },200)

        };
        $scope.startComputerGame = function(el){
            var element = el.target;
            $(".list-group-item").removeClass("active");
            $(element).closest(".list-group-item").addClass("active");
            $scope.playerPicker = false;
            $scope.computerGame = true;
            setTimeout(function(){
                var board,
                    game = new Chess();

                var onDragStart = function(source, piece, position, orientation) {
                    if (game.in_checkmate() === true || game.in_draw() === true ||
                        piece.search(/^b/) !== -1) {
                        return false;
                    }
                };

                var makeRandomMove = function() {
                    var possibleMoves = game.moves();

                    // game over
                    if (possibleMoves.length === 0) return;

                    var randomIndex = Math.floor(Math.random() * possibleMoves.length);
                    game.move(possibleMoves[randomIndex]);
                    board.position(game.fen());
                };

                var onDrop = function(source, target) {
                    // see if the move is legal
                    var move = game.move({
                        from: source,
                        to: target,
                        promotion: 'q' // NOTE: always promote to a queen for example simplicity
                    });

                    // illegal move
                    if (move === null) return 'snapback';
                    if(move){
                        if(game.in_checkmate() === true){
                            toastr.success('You win!');
                        } else if(game.in_draw() === true || game.in_threefold_repetition() === true || game.insufficient_material() === true){
                            toastr.success("It's a draw.")
                        }
                    }
                    // make random legal move for black
                    window.setTimeout(makeRandomMove, 250);
                };

                var onSnapEnd = function() {
                    board.position(game.fen());
                };

                var cfg = {
                    draggable: true,
                    position: 'start',
                    onDragStart: onDragStart,
                    onDrop: onDrop,
                    onSnapEnd: onSnapEnd
                };
                board = ChessBoard('boardComputer', cfg);
            },200);

        };

        $scope.giveUpGame = function(){
            $scope.handleWin($scope.Game);
            $scope.updateGameData($scope.Game);
            toastr.error('You give up #' + $scope.Game.id + " game.");
            $scope.Game = null;
            $scope.gameSelected = false;
        };
        $scope.showPosition = function(id){
            for(var i=0;i<$scope.Users.length;i++){
                if($scope.Users[i].id == id){
                    return i+1;
                }
            }
            return 0;
        };

        $scope.startSelectedGame = function(game){
            var index = 0;
            for( var i=0; i<$scope.Games.length; i++){
                if (game.id === $scope.Games[i].id){
                    index = i;
                }
            }
            var myGame = new Chess();
            myGame.load($scope.Games[index].fen);
            $scope.handleMove = function(source, target) {

                var move = myGame.move({
                    from: source,
                    to: target,
                    promotion: 'q'
                });

                if (move) {
                    if(myGame.in_checkmate() === true || myGame.in_draw() === true || myGame.in_threefold_repetition() === true || myGame.insufficient_material() === true){
                        $scope.Games[index].fen = myGame.fen();
                        $scope.Games[index].finished = true;

                        if(myGame.in_checkmate() === true){
                            $scope.handleWin($scope.Games[index]);
                        }
                        if(myGame.in_draw() === true || myGame.in_threefold_repetition() === true || myGame.insufficient_material() === true){
                            handleDraw($scope.Games[index]);
                        }
                        $scope.updateGameData($scope.Games[index]);
                        return false;
                    } else {
                        $scope.Games[index].fen = myGame.fen();
                        $scope.updateGameData($scope.Games[index]);
                    }
                } else {
                    return 'snapback';
                }
                return null;

            };
            function handleDraw(game){
                var User,Opponent;
                for(var i=0; i<$scope.Users; i++){
                    if($scope.Users[i].id == game.userId){
                        User = $scope.Users[i];
                    } else if($scope.Users[i].id == game.opponentId){
                      Opponent = $scope.Users[i];
                    }
                }

                User.points = User.points + 1;
                User.ties = User.ties + 1;
                Opponent.points = Opponent.points + 1;
                Opponent.ties = Opponent.ties + 1;

                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/users", data: User}, function(response){
                    console.log(response.data);
                });
                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/users", data: Opponent}, function(response){
                    console.log(response.data);
                });

                var messageToUser = {
                    text: "Game #" + game.id + " finished. Result: Tie",
                    creationtime: new Date().getTime(),
                    senderId: null,
                    reciverId: game.userId,
                    type: globalContext.messageTypes().tie
                };
                var messageToOpponent = {
                    text: "Game #" + game.id + " finished. Result: Tie",
                    creationtime: new Date().getTime(),
                    senderId: null,
                    reciverId: game.opponentId,
                    type: globalContext.messageTypes().tie
                };
                sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages", data: messageToUser}, function(response){
                    console.log(response.data);
                });
                sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages", data: messageToOpponent}, function(response){
                    console.log(response.data);
                });
            }

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
        $scope.updateGameData = function(game){
            delete game.opponentName;
            var myGame = new Chess();
            myGame.load(game.fen);
            if (myGame.turn() == 'w' && $scope.actualUser.id === game.userId || myGame.turn() == 'b' && $scope.actualUser.id === game.userId) {
                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/games" + "?userId=" + game.opponentId, data: game},function(response){
                    console.log(response);
                });
            } else if(myGame.turn() == 'b' && $scope.actualUser.id === game.opponentId || myGame.turn() == 'w' && $scope.actualUser.id === game.opponentId){
                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/games" + "?userId=" + game.userId, data: game},function(response){
                    console.log(response);
                });
            }
        };
        $scope.handleWin = function (game){
            var User = null;
            var Opponent = null;
            var messageToUser = null;
            var messageToOpponent = null;
            var myGame = new Chess();

            myGame.load(game.fen);

            for(var i=0; i<$scope.Users.length; i++){
                if($scope.Users[i].id == game.userId){
                    User = $scope.Users[i];
                }
                if($scope.Users[i].id == game.opponentId){
                    Opponent = $scope.Users[i];
                    console.log(Opponent);
                }
            }

            if (myGame.turn() == 'w' && User.id === game.userId || myGame.turn() == 'b' && User.id === game.userId) {
                User.loses += 1;
                Opponent.points += 3;
                Opponent.wins += 1;
                messageToUser = {
                    text: "Game #" + game.id + " finished. Result: Lost",
                    creationtime: new Date().getTime(),
                    senderId: null,
                    reciverId: game.userId,
                    type: globalContext.messageTypes().lost
                };
                messageToOpponent = {
                    text: "Game #" + game.id + " finished. Result: Win",
                    creationtime: new Date().getTime(),
                    senderId: null,
                    reciverId: game.opponentId,
                    type: globalContext.messageTypes().win
                };
                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/users", data: User}, function(response){
                    console.log(response.data);
                });
                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/users", data: Opponent}, function(response){
                    console.log(response.data);
                });
                sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages", data: messageToUser}, function(response){
                    console.log(response.data);
                });
                sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages", data: messageToOpponent}, function(response){
                    console.log(response.data);
                });
            } else if(myGame.turn() == 'b' && Opponent.id === game.opponentId || myGame.turn() == 'w' && Opponent.id === game.opponentId){
                User.loses += 1;
                Opponent.points += 3;
                Opponent.wins += 1;
                messageToUser = {
                    text: "Game #" + game.id + " finished. Result: Lost",
                    creationtime: new Date().getTime(),
                    senderId: null,
                    reciverId: game.opponentId,
                    type: globalContext.messageTypes().lost
                };
                messageToOpponent = {
                    text: "Game #" + game.id + " finished. Result: Win",
                    creationtime: new Date().getTime(),
                    senderId: null,
                    reciverId: game.userId,
                    type: globalContext.messageTypes().win
                };
                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/users", data: User}, function(response){
                    console.log(response.data);
                });
                sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/users", data: Opponent}, function(response){
                    console.log(response.data);
                });
                sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages", data: messageToUser}, function(response){
                    console.log(response.data);
                });
                sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages", data: messageToOpponent}, function(response){
                    console.log(response.data);
                });
            }
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
        $scope.sendInvite = function(user) {
            var date = new Date().getTime();
            var invite = {
                creationtime: date,
                reciverId: user.id,
                senderId: $scope.actualUser.id,
                type: "Game",
                playable: true,
                gameId: null
            };
            sharedProperties.sendData({
                method: 'POST',
                url: globalContext.mainURL() + '/invites',
                data: invite
            }, function () {
                toastr.success("Sent invite to " + user.login);
            });
        };
        $scope.sendRandomInvite = function(){
            var index = Math.floor((Math.random() * $scope.Users.length) + 1) - 1;
            while ($scope.Users[index].id == $scope.actualUser.id){
                index = Math.floor((Math.random() * $scope.Users.length) + 1) - 1;
            }
            $scope.sendInvite($scope.Users[index]);

        }
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
                window.location.href = "http://" + window.location.hostname + "/ChessOnline/";
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
        $scope.canAdd = false;
        $scope.showGame = false;
        $scope.showGames = true;
        $scope.init = function(){
            $('#playerGamesList').css('min-height', window.innerHeight - 150);
            $('#playerGamesList').css('max-height', window.innerHeight - 150);
            if(sharedProperties.getActualUser() != null){
                $scope.actualUser = JSON.parse(sessionStorage.getItem('actualUser'));
                $scope.showUser = sharedProperties.getShowUser();
                if($scope.showUser == null){
                    $scope.showUser = $scope.actualUser;
                }
                $scope.position = sharedProperties.getPosition();
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/games/" + $scope.showUser.id}, function(response){
                    $scope.Games = response.data;
                });
                sharedProperties.getData({method: "GET", url: globalContext.mainURL() + "/friends/check?userId=" + $scope.actualUser.id + "&friendId=" + $scope.showUser.id},
                    function(response){
                        $scope.canAdd = response.data;
                    });
                $scope.Users = sharedProperties.getUsers();
                if ($scope.Users.length === 0){
                    sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/users/"}, function(response){
                        $scope.Users = response.data;
                    });
                }
            } else {
                $scope.actualUser = JSON.parse(sessionStorage.getItem('actualUser'));
                window.location.href = "http://" + window.location.hostname + "/ChessOnline/";
            }

        };
        $scope.showUserGame = function(game){
            $scope.showGame = true;
            $scope.showGames = false;
            var playerGame = new Chess();
            playerGame.load(game.fen);
            setTimeout(function(){
                var cfg = {
                    draggable: false,
                    position: game.fen
                };
                var board = new ChessBoard('boardPlayer', cfg);
            },200);
        };
        $scope.backToList = function(){
            $scope.showGame = false;
            $scope.showGames = true;
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
        $scope.sendFriendInvite = function(user){
            var message = {
                creationtime: new Date().getTime(),
                reciverId: user.id,
                senderId: $scope.actualUser.id,
                text: $scope.actualUser.login + " want add you as friend. Do you accept the invitation?",
                type: globalContext.messageTypes().invite
            };
            sharedProperties.sendData({method: 'POST', url: globalContext.mainURL() + "/messages/", data: message}, function(){
                toastr.success("Sent invite to " + user.login);
                $scope.canAdd = false;
            });
        };
        $scope.sendInvite = function(user){
            var date =  new Date().getTime();
            var invite = {
                creationtime: date,
                reciverId: user.id,
                senderId: $scope.actualUser.id,
                type: "Game",
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

    /***********SETTINGS MODULE CONTROLLER***********/

    app.controller("settingsController", function($scope, sharedProperties, $route){
        $scope.actualUser = sharedProperties.getActualUser();
        $scope.Friends = [];
        $scope.Users = [];
        $scope.userSettings = false;
        $scope.friendsSettings = false;
        $scope.friendsSettings = false;
        $scope.remove = false;
        $scope.friendToRemove = {};
        $scope.changePassword = false;
        $scope.changeMail = false;
        $scope.deleteUser = false;
        $scope.useCase = "";
        $scope.init = function(){
            if ($scope.actualUser != null){
                sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/users"}, function(response){
                    $scope.Users = response.data;
                    sharedProperties.getData({method: 'GET', url: globalContext.mainURL() + "/friends/" + $scope.actualUser.id}, function(reponseFriends){
                        $scope.Friends = reponseFriends.data;
                    })
                })
            } else {
                window.location.href = "http://" + window.location.hostname + "/ChessOnline/";
            }
        };
        $scope.setUserSettings = function(){
            $scope.userSettings = !$scope.userSettings;
            $scope.friendsSettings = false;
            $("#userSettings").toggleClass("active");
            $("#friendsSettings").removeClass("active");
        };
        $scope.setFriendsSettings = function(){
            $scope.friendsSettings = !$scope.friendsSettings;
            $scope.userSettings = false;
            $("#friendsSettings").toggleClass("active");
            $("#userSettings").removeClass("active");
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
        $scope.setFriendToRemove = function(friend){
            console.log(friend);
            $scope.friendToRemove = friend;
            $scope.removeFriend = !$scope.removeFriend;
        };
        $scope.getRemoveLogin = function(){
            return $scope.getSenderLogin($scope.friendToRemove.id);
        };
        $scope.changePass = function(){
            $scope.changePassword = !$scope.changePassword;
            $scope.changeMail = false;
            $scope.deleteUser = false;
            $("#pass").toggleClass("active");
            $("#log").removeClass("active");
            $("#email").removeClass("active");
            $scope.useCase = "changePass";
        };
        $scope.changeLoginToNew = function(){
            $scope.changePassword = false;
            $scope.changeMail = false;
            $scope.deleteUser = !$scope.deleteUser;
            $("#log").toggleClass("active");
            $("#pass").removeClass("active");
            $("#email").removeClass("active");
            $scope.useCase = "deleteUser";
        };
        $scope.changeEmail = function(){
            $scope.changePassword = false;
            $scope.changeMail = !$scope.changeMail;
            $scope.deleteUser = false;
            $("#email").toggleClass("active");
            $("#log").removeClass("active");
            $("#pass").removeClass("active");
            $scope.useCase = "changeMail";
        };
        $scope.updateUserData = function(){
            var valid = false;
            var isDelete = false;
            switch ($scope.useCase){
                case "changeMail":
                    if($('#mail').val() === $('#mail2').val()){
                        if($('#mail').val() === $scope.actualUser.email){
                            toastr.error("Your actual e-mail address is the same.");
                            break;
                        } else {
                            $scope.actualUser.email = $('#mail').val();
                            valid = true;
                            break;
                        }
                    } else {
                        toastr.error("Error! Entered e-mails are different.");
                        break;
                    }
                    break;
                case "changePass":
                    if($("#pwd").val() === $("#pwd2").val()){
                        if($("#pwd").val() === $scope.actualUser.password){
                            toastr.error("Your actual password is the same.");
                            break;
                        } else {
                            $scope.actualUser.password = $("#pwd").val();
                            valid = true;
                            break;
                        }
                    } else {
                        toastr.error("Error! Entered passwords are different.");
                        break;
                    }
                    break;
                case "deleteUser":
                    if($("#pwdDelete").val() === $scope.actualUser.password){
                        valid = true;
                        isDelete = true;
                        break;
                    } else {
                        toastr.error("Error! Entered password is wrong.");
                        break;
                    }
                    break;
            }

            if(valid){
                if(!isDelete){
                    sharedProperties.sendData({method: 'PUT', url: globalContext.mainURL() + "/users/", data: $scope.actualUser}, function(){
                        toastr.success("Updated user data.");
                    });
                } else {
                    sharedProperties.sendData({method: 'DELETE', url: globalContext.mainURL() + "/users/" + $scope.actualUser.id}, function(){
                        window.location.href = "http://" + window.location.hostname + "/ChessOnline/";
                        toastr.success("You delete your account.");
                    });
                }

            }

        };
        $scope.removeSelectedFriend = function(){
            console.log($scope.friendToRemove);
            sharedProperties.sendData({method: 'DELETE', url: globalContext.mainURL() + "/friends/remove?userId=" + $scope.actualUser.id + "&friendId=" + $scope.friendToRemove.id}, function(){
                toastr.success("You remove " + $scope.friendToRemove.login + " from your friends list.");
                $scope.setFriendToRemove();
                $scope.friendToRemove = null;
                $scope.init();
            });
        }
    });

    app.service('sharedProperties', function($http) {
        var actualUser = JSON.parse(sessionStorage.getItem('actualUser'));
        var showUser = null;
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
