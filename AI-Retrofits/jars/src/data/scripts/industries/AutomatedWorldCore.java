package data.scripts.industries;
//AutomatedWorldCore

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.Pair;
/*
    objectives:
        -all fleets with a home world with AutomatedWorldCore will gain the AIRetrofit hullmod
        -reduce all mirean and crew production and demand to zero
            -replace with war robots and worker robots?
        -set world's population growth to zero.
            -only goes up when 'hazard pay' is on (building new drones)
            -goes down when demand to this building is not met

        -AI inspection resalts in bad things if succeeds.
            -larger change to succeed.
            -tries to staturate bombard world if they can? trys to send destroy world fleet?
            -declare war?
        -AI cores and improvements:
            -improvement: + stability, slower pop lose rate when low on res, higher gain when hazard pay
            -gamma core: +pop growth rate, +pop growth cost?
            -beta core: +ground defence rating, +reduced import cost of this building?
            -alpha core: +income? +output of other jobs?


 */

public class AutomatedWorldCore extends BaseIndustry {

    public void apply() {
        super.apply(true);

        int size = market.getSize();

        demand(Commodities.ORGANICS, size);

        supply(Commodities.DOMESTIC_GOODS, size);
        //supply(Commodities.SUPPLIES, size - 3);

        //if (!market.getFaction().isIllegal(Commodities.LUXURY_GOODS)) {
        if (!market.isIllegal(Commodities.LUXURY_GOODS)) {
            supply(Commodities.LUXURY_GOODS, size - 2);
        } else {
            supply(Commodities.LUXURY_GOODS, 0);
        }
        //if (!market.getFaction().isIllegal(Commodities.DRUGS)) {
        if (!market.isIllegal(Commodities.DRUGS)) {
            supply(Commodities.DRUGS, size - 2);
        } else {
            supply(Commodities.DRUGS, 0);
        }

        Pair<String, Integer> deficit = getMaxDeficit(Commodities.ORGANICS);

        applyDeficitToProduction(1, deficit,
                Commodities.DOMESTIC_GOODS,
                Commodities.LUXURY_GOODS,
                //Commodities.SUPPLIES,
                Commodities.DRUGS);

        if (!isFunctional()) {
            supply.clear();
        }
    }


    @Override
    public void unapply() {
        super.unapply();
    }

    @Override
    public String getCurrentImage() {
        float size = market.getSize();
        PlanetAPI planet = market.getPlanetEntity();
        if (planet == null || planet.isGasGiant()) {
            if (size <= SIZE_FOR_SMALL_IMAGE) {
                return Global.getSettings().getSpriteName("industry", "light_industry_orbital_low");
            }
            if (size >= SIZE_FOR_LARGE_IMAGE) {
                return Global.getSettings().getSpriteName("industry", "light_industry_orbital_high");
            }
            return Global.getSettings().getSpriteName("industry", "light_industry_orbital");
        }
        else
        {
            if (size <= SIZE_FOR_SMALL_IMAGE) {
                return Global.getSettings().getSpriteName("industry", "light_industry_low");
            }
            if (size >= SIZE_FOR_LARGE_IMAGE) {
                return Global.getSettings().getSpriteName("industry", "light_industry_high");
            }
        }

        return super.getCurrentImage();
    }

    @Override
    protected boolean canImproveToIncreaseProduction() {
        return true;
    }
}