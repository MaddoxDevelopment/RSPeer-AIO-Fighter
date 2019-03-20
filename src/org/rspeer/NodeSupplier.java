package org.rspeer;

import org.rspeer.framework.Node;
import org.rspeer.nodes.combat.BackToFightZone;
import org.rspeer.nodes.combat.FightNode;
import org.rspeer.nodes.food.EatNode;
import org.rspeer.nodes.food.GetFoodNode;
import org.rspeer.nodes.idle.IdleNode;
import org.rspeer.nodes.loot.BuryBones;
import org.rspeer.nodes.loot.DepositLootNode;
import org.rspeer.nodes.loot.LootNode;
import org.rspeer.nodes.progressive.ProgressionChecker;

public class NodeSupplier {

    public final Node EAT = new EatNode();
    public final Node GET_FOOD = new GetFoodNode();
    public final Node IDLE = new IdleNode();
    public final Node DEPOSIT_LOOT = new DepositLootNode();
    public final Node LOOT = new LootNode();
    public final Node PROGRESSION_CHECKER = new ProgressionChecker();
    public final Node BURY_BONES = new BuryBones();
    public final Node BACK_TO_FIGHT = new BackToFightZone();
    public final Node FIGHT = new FightNode();

}
