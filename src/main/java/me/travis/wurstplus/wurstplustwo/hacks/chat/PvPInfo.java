package me.travis.wurstplus.wurstplustwo.hacks.chat;

//Imports

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.wurstplustwo.util.WurstplusFriendUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTimer;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//Module

public class PvPInfo extends WurstplusHack {

    //Strenght notifier
    private final Set<EntityPlayer> str;


    //Module Info
    public PvPInfo() {
        super(WurstplusCategory.WURSTPLUS_CHAT);

        this.name = "PvP Info"; //Commands and Clickgui
        this.tag = "PvPInfo"; //Config and Arraylist
        this.description = "alerts some events"; //Useless but normally i add this
        this.str = Collections.newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }

    WurstplusSetting burrow = create("BurrowNotifier", "BurrowNotif", false);
    WurstplusSetting pearl = create("PearlNotifier", "PearlNotif", true);
    WurstplusSetting strenght = create("StrenghtNotifier", "StrenghtNotif", true);
    WurstplusSetting weakness = create("WeaknessNotifier", "WeakNotif", true);
    //WurstplusSetting chorus = create("ChorusNotifier", "ChorusNotifier", true);
    WurstplusSetting breakwarner = create("BreakWarner", "BreakWarner", true);
    WurstplusSetting distanceToDetect = this.create("BreakDist", "WarnerDistance", 2, 1, 5);
    //WurstplusSetting delay = create("BreakWarnerDelay", "delay", 5, 1, 10);


    List<Entity> knownPlayers = new ArrayList<>();
    List<Entity> burrowedPlayers = new ArrayList<>();

    // PearlNotifier
    private Entity enderPearl;
    private boolean flag;
    private final HashMap list = new HashMap();
    //weakness alert
    private boolean hasAnnounced = false;
    private final WurstplusTimer timer = new WurstplusTimer();
    private int delay;

    @Override
    public void enable() {
        this.flag = true;
    }

    @Override
    public void update() {
        // burrow
        if (burrow.get_value(true)) {
            for (Entity entity : mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer).collect(Collectors.toList())) {
                if (!(entity instanceof EntityPlayer)) {
                    continue;
                }

                if (!burrowedPlayers.contains(entity) && isBurrowed(entity)) {
                    burrowedPlayers.add(entity);
                    WurstplusMessageUtil.send_client_message(ChatFormatting.DARK_PURPLE + "" + ChatFormatting.BOLD + "Burrow Announcer " + ChatFormatting.DARK_AQUA + " > " + ChatFormatting.RESET + entity.getName() + " burrowed!");
                } else if (burrowedPlayers.contains(entity) && !isBurrowed(entity)) {
                    burrowedPlayers.remove(entity);
                    WurstplusMessageUtil.send_client_message(ChatFormatting.DARK_PURPLE + "" + ChatFormatting.BOLD + "Burrow Announcer " + ChatFormatting.DARK_AQUA + " > " + ChatFormatting.RESET + entity.getName() + " unburrowed!");
                }
            }
        }

        if (pearl.get_value(true)) {
            if (mc.world != null && mc.player != null) {
                this.enderPearl = null;
                Iterator var1 = mc.world.loadedEntityList.iterator();

                while (var1.hasNext()) {
                    Entity e = (Entity) var1.next();
                    if (e instanceof EntityEnderPearl) {
                        this.enderPearl = e;
                        break;
                    }
                }

                if (this.enderPearl == null) {
                    this.flag = true;
                } else {
                    EntityPlayer closestPlayer = null;
                    Iterator var5 = mc.world.playerEntities.iterator();

                    while (var5.hasNext()) {
                        EntityPlayer entity = (EntityPlayer) var5.next();
                        if (closestPlayer == null) {
                            closestPlayer = entity;
                        } else if (closestPlayer.getDistance(this.enderPearl) > entity.getDistance(this.enderPearl)) {
                            closestPlayer = entity;
                        }
                    }

                    if (closestPlayer == mc.player) {
                        this.flag = false;
                    }

                    if (closestPlayer != null && this.flag) {
                        String faceing = this.enderPearl.getHorizontalFacing().toString();
                        if (faceing.equals("west")) {
                            faceing = "east";
                        } else if (faceing.equals("east")) {
                            faceing = "west";
                        }

                        WurstplusMessageUtil.send_client_message(ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "Pearl Notifier" + ChatFormatting.DARK_AQUA + "> " + ChatFormatting.RED + closestPlayer.getName() + ChatFormatting.WHITE + " thrown a pearl heading " + faceing);
                        this.flag = false;

                    }
                }
            }
            // weakness
            if (weakness.get_value(true)) {
                if (mc.player.isPotionActive(MobEffects.WEAKNESS) && !hasAnnounced) {
                    hasAnnounced = true;
                    WurstplusMessageUtil.send_client_message(ChatFormatting.GRAY + "" + ChatFormatting.BOLD + "You now have weakness");
                }
                if (!mc.player.isPotionActive(MobEffects.WEAKNESS) && hasAnnounced) {
                    hasAnnounced = false;
                    WurstplusMessageUtil.send_client_message(ChatFormatting.GRAY + "" + ChatFormatting.BOLD + "You no longer have weakness");
                }

            }
            // streng
            if (strenght.get_value(true)) {
                for (final EntityPlayer player : mc.world.playerEntities) {
                    if (player.equals((Object) mc.player)) {
                        continue;
                    }
                    if (player.isPotionActive(MobEffects.STRENGTH) && !this.str.contains(player)) {
                        WurstplusMessageUtil.send_client_message(ChatFormatting.RED + "" + ChatFormatting.BOLD + "Strength Detect" + ChatFormatting.RESET + ChatFormatting.DARK_AQUA + " > " + ChatFormatting.RESET + player.getDisplayNameString() + " Has Strength");
                        this.str.add(player);
                    }
                    if (!this.str.contains(player)) {
                        continue;
                    }
                    if (player.isPotionActive(MobEffects.STRENGTH)) {
                        continue;
                    }
                    WurstplusMessageUtil.send_client_message(ChatFormatting.RED + "" + ChatFormatting.BOLD + "Strength Detect" + ChatFormatting.RESET + ChatFormatting.DARK_AQUA + " > " + ChatFormatting.RESET + player.getDisplayNameString() + " Has Ran Out Of Strength");
                    this.str.remove(player);
                }
            }
        }
    }// end of public void update

    private boolean isBurrowed(Entity entity) {
        BlockPos entityPos = new BlockPos((entity.posX), entity.posY, (entity.posZ));

        if (mc.world.getBlockState(entityPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(entityPos).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(entityPos).getBlock() == Blocks.WEB || mc.world.getBlockState(entityPos).getBlock() == Blocks.SKULL) {
            return true;
        }
        return false;
    }

    @EventHandler
    public Listener<WurstplusEventPacket.ReceivePacket> packetReceiveListener = new Listener<WurstplusEventPacket.ReceivePacket>(event -> {
        SPacketBlockBreakAnim packet;
        BlockPos pos;
        EntityPlayerSP player = mc.player;
        WorldClient world = mc.world;
        if (Objects.isNull((Object)player) || Objects.isNull((Object)world)) {
            return;
        }
        if (event.get_packet() instanceof SPacketBlockBreakAnim && this.pastDistance((EntityPlayer)player, pos = (packet = (SPacketBlockBreakAnim)event.get_packet()).getPosition(), this.distanceToDetect.get_value(1))) {
            this.sendChat();
        }
    }, new Predicate[0]);

    private boolean pastDistance(EntityPlayer player, BlockPos pos, double dist) {
        return player.getDistanceSqToCenter(pos) <= Math.pow(dist, 2.0);
    }

    public void sendChat() {
        if (this.breakwarner.get_value(true)) {
            WurstplusMessageUtil.send_client_message((Object)ChatFormatting.GRAY + ">>>" + (Object)ChatFormatting.RESET + (Object)ChatFormatting.RED + "BREAK WARNING!!!");
        }
        ++delay;
    }
    /**


    public class WurstplusNotifications
            extends WurstplusHack {
        WurstplusSetting breakwarner = this.create("BreakWarner", "BreakWarner", true);
        WurstplusSetting totempop = this.create("TotemPop", "TotemPop", true);
        WurstplusSetting coordexploit = this.create("CoordExploit", "CoordExploit", false);
        WurstplusSetting visualrange = this.create("VisualRange", "VisualRange", false);
        WurstplusSetting strength = this.create("Strength", "Strength", false);
        WurstplusSetting distanceToDetect = this.create("Break Distance", "WarnerDistance", 2, 1, 5);
        WurstplusSetting chatDelay = this.create("Chat Delay", "WarnerChatDelay", 18, 14, 25);
        private int delay;
        private final Set<EntityPlayer> str;
        private List<String> people;
        public static final Minecraft mc = Minecraft.getMinecraft();
        private HashMap<Entity, Vec3d> knownPlayers = new HashMap();
        private HashMap<String, Vec3d> tpdPlayers = new HashMap();
        public static final HashMap<String, Integer> totem_pop_counter = new HashMap();
        private int numTicks = 0;
        private int numForgetTicks = 0;
        public static ChatFormatting red = ChatFormatting.RED;
        public static ChatFormatting green = ChatFormatting.GREEN;
        public static ChatFormatting blue = ChatFormatting.BLUE;
        public static ChatFormatting grey = ChatFormatting.GRAY;
        public static ChatFormatting bold = ChatFormatting.BOLD;
        public static ChatFormatting reset = ChatFormatting.RESET;
        @EventHandler
        public Listener<WurstplusEventPacket.ReceivePacket> packetReceiveListener = new Listener<WurstplusEventPacket.ReceivePacket>(event -> {
            SPacketBlockBreakAnim packet;
            BlockPos pos;
            EntityPlayerSP player = WurstplusNotifications.mc.player;
            WorldClient world = WurstplusNotifications.mc.world;
            if (Objects.isNull((Object)player) || Objects.isNull((Object)world)) {
                return;
            }
            if (event.get_packet() instanceof SPacketBlockBreakAnim && this.pastDistance((EntityPlayer)player, pos = (packet = (SPacketBlockBreakAnim)event.get_packet()).getPosition(), this.distanceToDetect.get_value(1))) {
                this.sendChat();
            }
        }, new Predicate[0]);
        @EventHandler
        private final Listener<WurstplusEventPacket.ReceivePacket> packet_event = new Listener<WurstplusEventPacket.ReceivePacket>(event -> {
            SPacketEntityStatus packet;
            if (event.get_packet() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.get_packet()).getOpCode() == 35) {
                Entity entity = packet.getEntity((World)WurstplusNotifications.mc.world);
                int count = 1;
                if (totem_pop_counter.containsKey(entity.getName())) {
                    count = totem_pop_counter.get(entity.getName());
                    totem_pop_counter.put(entity.getName(), ++count);
                } else {
                    totem_pop_counter.put(entity.getName(), count);
                }
                if (entity == WurstplusNotifications.mc.player) {
                    return;
                }
                if (WurstplusFriendUtil.isFriend(entity.getName())) {
                    WurstplusMessageUtil.send_client_message((Object)grey + ">>>" + (Object)reset + (Object)green + (Object)bold + entity.getName() + (Object)reset + " has popped " + (Object)bold + count + (Object)reset + " totems.");
                } else {
                    WurstplusMessageUtil.send_client_message((Object)grey + ">>>" + (Object)reset + (Object)red + (Object)bold + entity.getName() + (Object)reset + " has popped " + (Object)bold + count + (Object)reset + " totems.");
                }
            }
        }, new Predicate[0]);

        public WurstplusNotifications() {
            super(WurstplusCategory.WURSTPLUS_CHAT);
            this.str = Collections.newSetFromMap(new WeakHashMap());
            this.name = "Notifications";
            this.tag = "Notifications";
            this.description = "notificatesss";
        }

        @Override
        public void enable() {
            this.people = new ArrayList<String>();
        }

        @Override
        public void update() {
            if (this.strength.get_value(true)) {
                for (EntityPlayer player : WurstplusNotifications.mc.world.playerEntities) {
                    if (player.equals((Object)WurstplusNotifications.mc.player)) continue;
                    if (player.isPotionActive(MobEffects.STRENGTH) && !this.str.contains((Object)player)) {
                        WurstplusMessageUtil.send_client_message((Object)ChatFormatting.RESET + player.getDisplayNameString() + (Object)ChatFormatting.RED + " Has Strength");
                        this.str.add(player);
                    }
                    if (!this.str.contains((Object)player) || player.isPotionActive(MobEffects.STRENGTH)) continue;
                    WurstplusMessageUtil.send_client_message((Object)ChatFormatting.RESET + player.getDisplayNameString() + (Object)ChatFormatting.GREEN + " Has Ran Out Of Strength");
                    this.str.remove((Object)player);
                }
            }
            if (this.coordexploit.get_value(true)) {
                if (this.numTicks >= 50) {
                    this.numTicks = 0;
                    for (Entity entity : WurstplusNotifications.mc.world.loadedEntityList) {
                        if (!(entity instanceof EntityPlayer) || entity.getName().equals(WurstplusNotifications.mc.player.getName())) continue;
                        Vec3d playerPos = new Vec3d(entity.posX, entity.posY, entity.posZ);
                        if (this.knownPlayers.containsKey((Object)entity) && Math.abs(this.knownPlayers.get((Object)entity).distanceTo(playerPos)) > 50.0 && Math.abs(WurstplusNotifications.mc.player.getPositionVector().distanceTo(playerPos)) > 100.0 && (!this.tpdPlayers.containsKey(entity.getName()) || this.tpdPlayers.get(entity.getName()) != playerPos)) {
                            WurstplusMessageUtil.send_client_message("Player " + entity.getName() + " has tp'd to " + WurstplusNotifications.vectorToString(playerPos, false));
                            this.saveFile(WurstplusNotifications.vectorToString(playerPos, false), entity.getName());
                            this.knownPlayers.remove((Object)entity);
                            this.tpdPlayers.put(entity.getName(), playerPos);
                        }
                        this.knownPlayers.put(entity, playerPos);
                    }
                }
                if (this.numForgetTicks >= 9000000) {
                    this.tpdPlayers.clear();
                }
                ++this.numTicks;
                ++this.numForgetTicks;
            }
            if (this.totempop.get_value(true)) {
                for (EntityPlayer player : WurstplusNotifications.mc.world.playerEntities) {
                    if (!totem_pop_counter.containsKey(player.getName()) || !player.isDead && !(player.getHealth() <= 0.0f)) continue;
                    int count = totem_pop_counter.get(player.getName());
                    totem_pop_counter.remove(player.getName());
                    if (player == WurstplusNotifications.mc.player) continue;
                    if (WurstplusFriendUtil.isFriend(player.getName())) {
                        WurstplusMessageUtil.send_client_message((Object)grey + ">>> " + (Object)reset + (Object)green + player.getName() + (Object)reset + " died after popping " + (Object)bold + count + (Object)reset + " totems");
                        continue;
                    }
                    WurstplusMessageUtil.send_client_message((Object)grey + ">>> " + (Object)reset + (Object)red + player.getName() + (Object)reset + " died after popping " + (Object)bold + count + (Object)reset + " totems");
                }
            }
            if (this.visualrange.get_value(true)) {
                if (WurstplusNotifications.mc.world == null | WurstplusNotifications.mc.player == null) {
                    return;
                }
                ArrayList<String> peoplenew = new ArrayList<String>();
                List playerEntities = WurstplusNotifications.mc.world.playerEntities;
                for (Entity e : playerEntities) {
                    if (e.getName().equals(WurstplusNotifications.mc.player.getName())) continue;
                    peoplenew.add(e.getName());
                }
                if (peoplenew.size() > 0) {
                    for (String name : peoplenew) {
                        if (this.people.contains(name)) continue;
                        if (WurstplusFriendUtil.isFriend(name)) {
                            WurstplusMessageUtil.send_client_message((Object)ChatFormatting.GRAY + ">>> " + (Object)ChatFormatting.RESET + "A player named  " + (Object)ChatFormatting.RESET + (Object)ChatFormatting.GREEN + name + (Object)ChatFormatting.RESET + " Enter render distance");
                        } else {
                            WurstplusMessageUtil.send_client_message((Object)ChatFormatting.GRAY + ">>> " + (Object)ChatFormatting.RESET + "A player named " + (Object)ChatFormatting.RESET + (Object)ChatFormatting.RED + name + (Object)ChatFormatting.RESET + " Enter render distance");
                        }
                        this.people.add(name);
                    }
                }
            }
        }

        public static String vectorToString(Vec3d vector, boolean includeY) {
            StringBuilder builder = new StringBuilder();
            builder.append('(');
            builder.append((int)Math.floor(vector.x));
            builder.append(", ");
            if (includeY) {
                builder.append((int)Math.floor(vector.y));
                builder.append(", ");
            }
            builder.append((int)Math.floor(vector.z));
            builder.append(")");
            return builder.toString();
        }

        public void saveFile(String pos, String name) {
            try {
                File file = new File("./ChachooxWare/coordexploit.txt");
                file.getParentFile().mkdirs();
                PrintWriter writer = new PrintWriter(new FileWriter(file, true));
                writer.println("name: " + name + " coords: " + pos);
                writer.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        private boolean pastDistance(EntityPlayer player, BlockPos pos, double dist) {
            return player.getDistanceSqToCenter(pos) <= Math.pow(dist, 2.0);
        }

        public void sendChat() {
            if (this.breakwarner.get_value(true)) {
                WurstplusMessageUtil.send_client_message((Object)ChatFormatting.GRAY + ">>>" + (Object)ChatFormatting.RESET + (Object)ChatFormatting.RED + "BREAK WARNING!!!");
            }
            ++this.delay;
        }

        public String getPlayer() {
            EntityPlayer e;
            List entities = WurstplusNotifications.mc.world.playerEntities.stream().filter(entityPlayer -> !WurstplusFriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList());
            Iterator var2 = entities.iterator();
            do {
                if (!var2.hasNext()) {
                    return "";
                }
                e = (EntityPlayer)var2.next();
            } while (e.isDead || e.getHealth() <= 0.0f || e.getName().equals(WurstplusNotifications.mc.player.getName()) || !(e.getHeldItemMainhand().getItem() instanceof ItemTool));
            return e.getName();
        }
    }*/


}