let MODEL = {
    // Data of the app
    service: undefined,
    data: {
        suffixes: [],
        card: {
            inputValue: "",
            tabs: {}
        }
    },
    hasChanged: {
        card: {
            inputValue: false,
            tabs: false
        }
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
                        this.data.card.tabs = {
                            data: message.data,
                            url: message.url
                        };
                    } else {
                        this.data.card.tabs = {};
                    }
                    this.hasChanged.card.tabs = true;
                    STATE.represent(this);
                    break;
                case 'info':
                    if (message.succeed)
                        this.data.suffixes = message.data;
                    break;
            }
        }
        this.hasChanged.card.inputValue = true;
        this.hasChanged.card.tabs = true;
        STATE.represent(this);
    },

    /**
     * Change the value input field
     */
    getNewURLInfo(value) {
        this.data.card.inputValue = value;

        if (this.isValidURL()) {
            console.log('send request');
            this.service.send(JSON.stringify({
                type: "request",
                data: {
                    url: value,
                    dns: "dns://8.8.8.8"
                }
            }));
        } else {
            this.data.card.tabs = {};
            this.hasChanged.card.tabs = true;
        }
        this.hasChanged.card.inputValue = true;
        STATE.represent(this);
    },

    isValidURL() {
        if (!this.data.card.inputValue.match(/[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)?/gi))
            return false;
        for (let elt of this.data.suffixes) {
            if (this.data.card.inputValue.endsWith('.' + elt))
                return true;
        }
        return false;
    }
}