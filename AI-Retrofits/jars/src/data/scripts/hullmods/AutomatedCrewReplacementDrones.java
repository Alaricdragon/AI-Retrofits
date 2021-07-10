package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
/*
    changes to consider:
        -(done)make it replace crew with heavy matchinary (for drones)
        -(not done)drop max CR when mod instaled, up to 50%, depending on hullsize and 'drones' added (drones == (MaxCrew - MinCrew) / 10)
        -(not done)lower max crew by MaxCrew - MinCrew
    -get a description that shows the number of 'drones' that i will be getting, as well as all other costs
        -(not done)raise CR recoverycost while instaled?

 */


public class AutomatedCrewReplacementDrones extends BaseHullMod {
    static int DronePerCrew = 10;
    float ReplacedCrew;
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        //set this value to (maxcrew - mincrew) / 10; example: (500 = 50. 10 = 1)
        float MinCrew = stats.getVariant().getHullSpec().getMinCrew();
        float MaxCrew = stats.getVariant().getHullSpec().getMaxCrew();
        stats.getDynamic().getMod(Stats.getSurveyCostReductionId(Commodities.CREW)).modifyFlat(id,(MaxCrew - MinCrew) / DronePerCrew);
        //stats.getDynamic().getMod(Stats.getSurveyCostReductionId(Commodities.CREW)).modifyFlat(id,500);
        //stats.getDynamic().getMod(Stats.getSurveyCostReductionId(Commodities.SUPPLIES)).modifyFlat(id, 300);
    }

    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        //int temp = (int) (ReplacedCrew / DronePerCrew);
        switch(index) {
            case 0:
                return "" + DronePerCrew;
            case 1:
                return "" + (int)ReplacedCrew;
            case 2:
                return "" + (int)(ReplacedCrew / DronePerCrew);
        }
        return null;
    }
    public boolean isApplicableToShip(ShipAPI ship/*, MutableCharacterStatsAPI wat*/){
        float MinCrew = ship.getVariant().getHullSpec().getMinCrew();
        float MaxCrew = ship.getVariant().getHullSpec().getMaxCrew();
        ReplacedCrew = (MaxCrew - MinCrew);
        return ship != null/* && cost <= unusedOP*/&& ReplacedCrew >= DronePerCrew;
    }
    public String getUnapplicableReason(ShipAPI ship) {
        float MinCrew = ship.getVariant().getHullSpec().getMinCrew();
        float MaxCrew = ship.getVariant().getHullSpec().getMaxCrew();
        ReplacedCrew = (MaxCrew - MinCrew);
        if(!(ReplacedCrew >= DronePerCrew)){
            return "Need at least " + DronePerCrew + " spare crew on ship to replace crew. you have " + ReplacedCrew;
        }
        return super.getUnapplicableReason(ship);
    }
}
