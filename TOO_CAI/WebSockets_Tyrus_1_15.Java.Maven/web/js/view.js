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
            let data = MODEL.card.leftCard.tabs[MODEL.card.leftCard.currentTab].data;
            let cardData = "";
            Object.keys(data).sort().forEach( (key) => {
                // Les champs vide = je connais pas le format des info
                // TODO completer le switch

                cardData += `<h5 class="p-1 mb-2 bg-primary text-white">${key}</h5>`;
                switch (key) {
                    case 'A':
                    case 'AAAA':
                        cardData += `
                        <div class="table-responsive">
                            <table class="col-12 table table-sm table-bordered">
                                <thead>
                                    <th class="align-middle text-center col-2"> Address </th>
                                </thead>
                                ${data[key].map( (elt) => {
                                    return `<tr><td>${elt}</td></tr>`
                                }).join('\n')}
                            </table>
                        </div>
                        `;
                        break;
                    case 'CNAME':
                        break;
                    case 'MS':
                        cardData += `
                        <div class="table-responsive">
                            <table class="col-12 table table-sm table-bordered">
                                <thead>
                                    <th class="align-middle text-center col-2"> Pref </th>
                                    <th class="align-middle text-center col-2"> Hostname </th>
                                </thead>
                                ${data[key].map( (elt) => {
                                    elt = elt.split(' ');
                                    return `<tr><td>${elt[0]}</td><td>${elt[1]}</td></tr>`
                                }).join('\n')}
                            </table>
                        </div>
                        `;
                        break;

                    case 'NS':
                        cardData += `
                        <div class="table-responsive">
                            <table class="col-12 table table-sm table-bordered">
                                <thead>
                                    <th class="align-middle text-center col-2"> NS </th>
                                </thead>
                                ${data[key].map( (elt) => {
                                    return `<tr><td>${elt}</td></tr>`
                                }).join('\n')}
                            </table>
                        </div>
                        `;
                        break;
                    case 'PTR':
                        break;
                    case 'SRV':
                        break;
                    case 'SOA':
                        cardData += `
                        <div class="table-responsive">
                            <table class="col-12 table table-sm table-bordered">
                                <thead>
                                    <th class="align-middle text-center col-2"> Primary NS </th>
                                    <th class="align-middle text-center col-2"> Responsible Email </th>
                                </thead>
                                ${data[key].map( (elt) => {
                                    elt = elt.split(' ');
                                    return `<tr><td>${elt[0]}</td><td>${elt[1]}</td></tr>`
                                }).join('\n')}
                            </table>
                        </div>
                        `;
                        break;
                    case 'TXT':
                        cardData += `
                        <div class="table-responsive">
                            <table class="col-12 table table-sm table-bordered">
                                <thead>
                                    <th class="align-middle text-center col-2"> Record </th>
                                </thead>
                                ${data[key].map( (elt) => {
                                    return `<tr><td>${elt}</td></tr>`
                                }).join('\n')}
                            </table>
                        </div>
                        `;
                        break;
                    case 'HINFO':
                        break;
                    case 'NAPTR':
                        break;
                }
            });
            return cardData;
        }
    },

    rightCardTabs(MODEL) {

    },
    display(MODEL, STATE) {

    }

}