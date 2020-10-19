let webSocketService = {
    service: "",

    /**
     *  check if the input is an url and send an information request to the webSocket
     *  if the input is not an url display an error
     */
    getDNSInformation() {
        // get the document input
        let input = document.getElementById('inputText');
        let inputValue = input.value;

        if(inputValue) {
            if (inputValue.match(/[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)?/gi)) {
                this.service.send(JSON.stringify({
                    type: "Request",
                    value: inputValue
                }));
                console.log("Request Send to the WebSocketServer");
            } else {
                window.confirm("It's not a url");
            }
        }
    },

    displayDNSInformation(replyData) {

    }

}