let STATE = {
    leftCard: {
        tabs: "",
        inputField: "",
        data: "",
        footer: ""
    },
    rightCard: {
      tabs: "",
      checkbox: "",
      data: "",
      footer: ""
    },
    logButton: "",

    represent(MODEL) {
        let hasChanged = MODEL.hasChanged;
        if (hasChanged.card.leftCard.tabs) {
            this.leftCard.tabs = VIEW.leftCard.tab(MODEL);
        }
        if (hasChanged.card.leftCard.inputValue) {
            this.leftCard.inputField = VIEW.leftCard.inputField(MODEL);
        }
        if (hasChanged.card.leftCard.currentTab) {
            this.leftCard.data = VIEW.leftCard.currentCard(MODEL);
        }
        if (hasChanged.isLog) {
            this.leftCard.footer = VIEW.leftCard.footer(MODEL);
            this.rightCard.footer = VIEW.rightCard.footer(MODEL);
            this.logButton = VIEW.logButton(MODEL);
        }
        // TODO Fonction d'affichage

        this.display(MODEL);
    },

    display(MODEL) {
        VIEW.display(MODEL, STATE);
    }
}