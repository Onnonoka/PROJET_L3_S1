let MODEL = {
    // Data of the app
    service: undefined,
    data: {
        suffixes: [],
        card: {
            leftCard: {
                inputValue: "",
                tabs: [],
                currentTab: 0
            },
            rightCard: {}
        },
        isLog: false,
    },
    hasChanged: {
        card: {
            leftCard: {
                inputValue: false,
                tabs: false,
                currentTab: false
            },
            rightCard: {
                inputValue: false,
                tabs: false,
                currentTab: false
            }
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
            console.log("Message from Java: ", event.data);

            let message = JSON.parse(event.data);
            switch (message.type) {
                case "auth" :
                    if (message.succeed) {
                        MODEL.data = message.data;
                        this.data.isLog = true;
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Wrong login or password'
                        }).then( () => {
                            MODEL.login();
                        });
                    }
                    break;

                case "newAuth":
                    // If the creation of the account succeed
                    if (message.succeed) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Success',
                            text: 'The account has been created.',
                        }).then( () => {
                            MODEL.login();
                        });
                    // if is not
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'The account already exist.'
                        }).then( () => {
                            MODEL.signIn();
                        });
                    }
                    break;

                case "save":
                    // TODO traiter le message recu de java
                    break;

                case "reply" :
                    let dataPos = this.data.card.leftCard.tabs.map(elt => {return elt.url}).indexOf(message.data.url); // return undefined if not find
                    if (dataPos != -1) {
                        this.data.card.leftCard.tabs[dataPos].data = message.data;
                    } else {
                        this.data.card.leftCard.tabs.push({
                            data: message.data,
                            url: message.url
                        });
                    }
                    break;
                case 'info':
                    // TODO traiter le message recu de java
                    break;
            }
        }
    },

    /**
     * Open the login screen and send an auth request
     */
    login() {
        Swal.fire({
            title: 'Login',
            html:
                'Username : <input id="username" class="swal2-input">' +
                'password : <input id="password" class="swal2-input">',
            preConfirm: () => {
                return {
                    username: document.getElementById('username').value,
                    password: document.getElementById('password').value
                };
            }
        }).then(function (result) {
            console.log(result);
            if (result.isConfirmed) {
                // TODO verifier que username et password ne contienne pas de valeur chiante et qu'il ne sont pas null
                MODEL.service.send(JSON.stringify({
                    type: "auth",
                    data: result.value
                }));
            }
        });
    },

    /**
     * Open the sign in screen and send an auth creation request
     */
    signIn() {
        Swal.fire({
            title: 'Sign in',
            html:
                'Username : <input id="username" class="swal2-input">' +
                'password : <input id="password" class="swal2-input">',
            preConfirm: () => {
                return {
                    username: document.getElementById('username').value,
                    password: document.getElementById('password').value
                };
            }
        }).then(function (result) {
            console.log(result);
            if (result.isConfirmed) {
                // TODO verifier que username et password ne contienne pas de valeur chiante et qu'il ne sont pas null
                MODEL.service.send(JSON.stringify({
                    type: "newAuth",
                    data: result.value
                }));
            }
        });
    },

    /**
     * Change the value input field
     */
    getNewURLInfo(value) {
        console.log(value);
        this.data.card.leftCard.inputValue = value;

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