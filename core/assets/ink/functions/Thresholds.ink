== function statFlavors(type) ==
    { type:
        - "STAMINA":
            ~ return "fully exhausted,exhausted,fatigued,winded,steady,rested,fully rested"
        - "HIT_POINTS":
            ~ return "barely alive,severely injured,badly injured,injured,lightly injured,good,excellent"
        - "MANA":
            ~ return "drained,empty,low,depleted,charged,surging,overflowing"
        - "STRENGTH":
            ~ return "feeble,weak,soft,average,strong,mighty,herculean"
        - "DEXTERITY":
            ~ return "clumsy,awkward,slow,average,nimble,agile,acrobatic"
        - "CONSTITUTION":
            ~ return "sickly,frail,delicate,average,tough,resilient,unbreakable"
        - "INTELLIGENCE":
            ~ return "mindless,dim,dull,average,smart,brilliant,genius"
        - "WISDOM":
            ~ return "foolish,naive,unaware,average,perceptive,wise,enlightened"
        - "CHARISMA":
            ~ return "repulsive,awkward,plain,average,charming,magnetic,mesmerizing"
    }