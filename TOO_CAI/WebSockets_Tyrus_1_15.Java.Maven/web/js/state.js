let STATE = {
    card: {
        tab: "",
        inputField: "",
        data: ""
    },

    represent(MODEL) {
        let hasChanged = MODEL.hasChanged;
        if (hasChanged.card.tabs) {
            this.card.tab = VIEW.card.tab(MODEL);
            this.card.data = VIEW.card.card(MODEL);
        }
        if (hasChanged.card.inputValue) {
            this.card.inputField = VIEW.card.inputField(MODEL);
        }

        VIEW.display(this);
    }
}