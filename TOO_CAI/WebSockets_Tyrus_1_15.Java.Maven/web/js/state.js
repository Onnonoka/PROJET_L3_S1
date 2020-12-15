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
        if (hasChanged.card.tabs) {
            this.tabs = VIEW.leftCard.tab(MODEL);
        }
        if (hasChanged.card.inputValue) {
            this.inputField = VIEW.leftCard.inputField(MODEL);
        }
        if (hasChanged.card.currentTab) {
            this.leftCard.data = VIEW.leftCard.currentCard(MODEL);
        }
        // TODO Fonction d'affichage

        this.display(MODEL);
    },

    display(MODEL) {
        VIEW.display(MODEL, STATE);
    }
}