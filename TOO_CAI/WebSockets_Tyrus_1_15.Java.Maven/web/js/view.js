let VIEW = {

    leftCard: {
        tab(MODEL) {
            let leftCard = MODEL.data.card.leftCard;
            let tabs = "";
            if (leftCard.tabs.length > 0) {
                leftCard.tabs.forEatch( (elt, index) => {
                    tabs += `<li class="nav-item">
                        <a class="nav-link ${leftCard.currentTab === index ? "active" : ""}">
                           ${elt.url}
                        </a>
                     </li>`
                });
            } else {
                tabs = `<li class="nav-item">
                <a class="nav-link active">
                    New domain
                </a>
            </li>`;
            }
            return tabs;
        },

        inputField(MODEL) {
            return `<div class="input-group-append">
                <span class="input-group-text">
                    Enter domain name :
                </span>
            </div>
            <input value="${MODEL.leftCard.inputValue}" type="text" class="form-control ${
                !MODEL.card.leftCard.inputValue.match(/[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)?/gi)? "border-danger" : ""
            }" placeholder="www.WhoIs.com" onchange="MODEL.getNewURLInfo(value)" />`;
        },

        currentCard(MODEL) {

            return cardData;
        }
    },

    rightCard: {

    },

    loginButton(MODEL) {
        if(MODEL.isLog) {
            return `<div id="logButton" class="col-lg-3">
                <div class="btn-group">
                    <button type="button" class="btn btn-outline-danger" onclick="MODEL.disconnect()">Sign in</button>
                </div>
            </div>`;
        } else {
            return `<div id="logButton" class="col-lg-3">
                <div class="btn-group">
                    <button type="button" class="btn btn-outline-primary" onclick="MODEL.signIn()">Sign in</button>
                    <button type="button" class="btn btn-primary" onclick="MODEL.login()">Login</button>
                </div>
            </div>`;
        }
    },

    display(MODEL, STATE) {

    }

}