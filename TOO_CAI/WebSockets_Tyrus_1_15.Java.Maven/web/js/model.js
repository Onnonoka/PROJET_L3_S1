/*
    ************************************************************
    |  Script ecrit par Dymko Frédéric, Cazaux Axel            |
    |  L3 informatique                                         |
    |  Projet TOO/CAI                                          |
    ************************************************************
*/

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

            let message = JSON.parse(event.data);
            if (!message.succeed) {
                this.data.card.tabs = {};
                this.hasChanged.card.tabs = true;
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    html: `<b>Something went wrong!</b><br/>${message.data.details}`
                });
                STATE.represent(this);
            } else {
                switch (message.type) {
                    case "reply" :
                        this.data.card.tabs = {
                            data: message.data,
                            url: message.url
                        };
                        this.hasChanged.card.tabs = true;
                        STATE.represent(this);
                        break;
                    case 'info':
                        this.data.suffixes = message.data;
                        break;
                }
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
        if (this.data.suffixes.length > 0) {
            let elt = 0;
            while (elt < this.data.suffixes.length && this.data.card.inputValue.endsWith('.' + this.data.suffixes[elt]))
                elt++;
            return elt < this.data.suffixes.length;
        }
        return true;
    }
}