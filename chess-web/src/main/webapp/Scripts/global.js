var global = global || {};

global.context = function(){
    var MessageTypes = {
        users: "User",
        system: "System"
    };
    return {
        startFEN: function(){
            return "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        },
        mainURL: function(){
            return "http://localhost:8080/ChessRests";
        },
        messageConfig: function(){
            return toastr.options = {
                "closeButton": false,
                "debug": false,
                "newestOnTop": true,
                "progressBar": false,
                "positionClass": "toast-bottom-full-width",
                "preventDuplicates": true,
                "onclick": null,
                "showDuration": "300",
                "hideDuration": "1000",
                "timeOut": "3000",
                "extendedTimeOut": "1000",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut"
            }
        },
        acceptInviteText: function(user,gameId){
            return user + " accepted your invite. Created game #" + gameId + ".";
        },
        messageTypes: function(){
            return MessageTypes;
        }
    }
};