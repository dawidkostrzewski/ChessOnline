<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>ChessOnline</title>
    <meta charset="utf-8">

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="Styles/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="Styles/style.css">
    <link rel="icon" href="./Styles/favicon.png">
    <link rel="stylesheet" href="Styles/toastr.min.css">
    <link rel="stylesheet" href="Styles/animate.css">
    <script src="Scripts/jquery-2.1.4.js"></script>
    <script src="Scripts/bootstrap.min.js"></script>
    <script src="Scripts/angular.min.js"></script>
    <script src="Scripts/angular-ui-router.js"></script>
    <script src="Scripts/angular-route.min.js"></script>
    <script src="Scripts/main.js"></script>
    <script src="Scripts/chess.js"></script>
    <script src="Scripts/chessboard-0.3.0.js"></script>
    <link rel="stylesheet" href="Styles/chessboard-0.3.0.css">
    <script src="Scripts/global.js"></script>
    <script src="Scripts/toastr.min.js"></script>
    <script src="Scripts/cometd.js"></script>
    <script src="Scripts/jquery.cometd.js"></script>

</head>
<body onload="init()">
<nav class="navbar navbar-inverse" data-ng-controller="navController">
    <div class="container-fluid">
        <ul class="nav navbar-nav">
            <li class="active" id="Home" data-ng-click="setActiveModule('Home')"><a href="#/"><i class="fa fa-home"></i> Home</a></li>
            <li id="Profile" data-ng-click="setActiveModule('Profile')"><a href="#Profile/{{actualUser.id}}"><i class="fa fa-user"></i> Profile</a></li>
            <li id="Games" data-ng-click="setActiveModule('Games')"><a href="#Games"><i class="fa fa-gamepad"></i> Games</a></li>
            <li id="Ranking" data-ng-click="setActiveModule('Ranking')"><a href="#Ranking"><i class="fa fa-bars"></i> Ranking</a></li>
            <li id="Settings" data-ng-click="setActiveModule('Settings')"><a href="#"><i class="fa fa-cogs"></i> Settings</a></li>
        </ul>
        <ul class="pull-right nav navbar-nav">
            <li class="pull-right"><a><div class="btn-xs btn-danger" data-ng-click="logOut()" style="cursor: pointer;"><i class="fa fa-sign-out"></i></div></a></li>
            <li class="pull-right"><a href="#Profile/{{actualUser.id}}">{{actualUser.login}}</a></li>
        </ul>
    </div>
</nav>
<div class="container-fluid mainContainer" data-ng-view>

</div>
</body>
</html>
