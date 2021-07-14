package me.travis.mapeadoh.clientstuff.salhack;


public class EventPlayerJoin extends MinecraftEvent {
    private String _name;
    private String _id;

    public EventPlayerJoin(String name, String id)
    {
        _name = name;
        _id = id;
    }

    public String getName()
    {
        return _name;
    }

    public String getId()
    {
        return _id;
    }
}