/*
    ************************************************************
    |  Script ecrit par Dymko Frédéric, Cazaux Axel            |
    |  L3 informatique                                         |
    |  Projet TOO/CAI                                          |
    ************************************************************
*/

let VIEW = {

    card: {
        tab(MODEL) {
            return `<li class="nav-item">
                        <a class="nav-link active">
                            ${MODEL.data.card.tabs.url || "New Domain"}
                        </a>
                    </li>`;
        },

        inputField(MODEL) {
            return `<div class="input-group-append">
                        <span class="input-group-text">
                            Enter domain name :
                        </span>
                    </div>
                    <input value="${MODEL.data.card.inputValue}" type="text" class="form-control ${
                        MODEL.isValidURL() || MODEL.data.card.inputValue === "" ? "" : "border-danger"
                    }" placeholder="www.WhoIs.com" onchange="MODEL.getNewURLInfo(value)" />`;
        },

        card(MODEL) {
            let data = MODEL.data.card.tabs.data;
            if (data) {
                let card = "";
                Object.keys(data).sort().forEach((key) => {
                    card += `<div class="">
                                <h5 class="p-1 mb-2 bg-primary text-white">${key}</h5>
                                <div class="table-responsive">
                                    <table class="table table-sm">
                                        ${data[key].map((elt) => {
                                            return `<tr class="d-flex"><td class="col-2" style=""><b>${key}</b></td><td class="col-10">${elt}</td></tr>`;
                                        }).join(' ')}
                                    </table>
                                </div>
                            </div>
                            <br>`;
                });
                return card;
            }
            let dataType = ["A", "AAAA", "CNAME", "HINFO", "MX", "NAPTR", "NS", "PTR", "SOA", "SRV", "TXT"];
            return dataType.map((type) => {
                return `<div class="">
                            <h5 class="p-1 mb-2 bg-primary text-white">${type}</h5>
                            No data found
                        </div>
                        <br>`;
            }).join('\n');
        }
    },

    display(STATE) {
        document.getElementById('cardTab').innerHTML = STATE.card.tab;
        document.getElementById('inputField').innerHTML = STATE.card.inputField;
        document.getElementById('cardData').innerHTML = STATE.card.data;
    }

}