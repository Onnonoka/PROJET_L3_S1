let MODEL = {
    // Data of the app
    service: undefined,
    data: {
        suffixes: [],
        card: {
            inputValue: "",
            tabs: {},
            currentTab: 0
        },
        isLog: false,
    },
    hasChanged: {
        card: {
            inputValue: false,
            tabs: false,
            currentTab: false
        },
        isLog: false
    },

    // Actions of the app
    /**
     * The app start there!!
     * Initialise all variable needed for the app
     */
    init() {
        this.service = new WebSocket("ws://localhost:1963/FranckBarbier/WebSockets_illustration");
        this.service.onmessage = (event) => {
            console.log("Message from Java: ", JSON.parse(event.data));

            let message = JSON.parse(event.data);
            switch (message.type) {
                case "reply" :
                    if (message.succeed) {
                        this.data.card.tabs.data = {
                            data: message.data,
                            url: message.url
                        };
                    }
                    break;
                case 'info':
                    // TODO traiter le message recu de java
                    break;
            }
        }
    },

    /**
     * Change the value input field
     */
    getNewURLInfo(value) {
        console.log(value);
        this.data.card.inputValue = value;

        if (value.match(/[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)?/gi)) {
            this.service.send(JSON.stringify({
                type: "request",
                data: {
                    url: value,
                    dns: "dns://8.8.8.8"
                }
            }));
        }
    }



}