== function sun_event(sunPosition) ==
    // Test function

    { sunPosition:
        - 0:
            { shuffle: // Sunrise
                - ~ return "The heavy shroud of night retreats as the sun’s golden eye slowly opens upon the world."
                - ~ return "A golden hue spills across the horizon as the sun begins its ascent."
                - ~ return "The lingering chill of night begins to lift as the sun’s rim peeks above the world’s edge."
            }
        - 1: 
            { shuffle: // Noon
                - ~ return "The sun hangs high in the sky, bathing everything in brilliant white light."
                - ~ return "Light pours straight down from the vault of the sky; the day has reached its boiling point."
                - ~ return "The sun claims its highest throne, marking the silent transition from morning to afternoon."
            }
        - 2:
            { shuffle: // Sunset
                - ~ return "The last sliver of the sun vanishes, leaving a cold, pale glow where the day once stood."
                - ~ return "The horizon bruises with crimson and violet as the world prepares for the coming dark."
                - ~ return "The sun dips low, bleeding a deep copper light across the land before the night claims the sky."
            }
    }
    

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