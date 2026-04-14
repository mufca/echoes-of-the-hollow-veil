== function movementMessage(direction, isPresent) ==
    { isPresent:
        ~ return "You move {direction}."
    - else:
        { shuffle:
            - ~ return "You don't see any exit to the {direction}."
            - ~ return "You head {direction}, but after a while you realize there is no way out in that direction."
            - ~ return "There is no exit in sight to the {direction}."
        }
    }