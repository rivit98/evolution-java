package agh.po.ewolucja;

enum NetOptions{
    PUTGRASS,
    PUTANIMAL,
    DELANIMAL,
    MOVEANIMAL,
    CREATEWORLD
}

public class NetPacketEncoder {
    public NetPacketEncoder(){

    }

    public String encode(NetOptions option, Object object){
        StringBuilder builder = new StringBuilder();
        builder.append(option.toString());
        builder.append(" ");

        switch (option){
            case PUTGRASS: {
                Grass g = (Grass) object;
                builder.append(0);
                builder.append(" ");
                builder.append(g.getPosition().x);
                builder.append(" ");
                builder.append(g.getPosition().y);
                break;
            }
            case PUTANIMAL:
            case DELANIMAL: {
                Animal a = (Animal) object;
                builder.append(a.getAnimalID());
                builder.append(" ");
                builder.append(a.getPosition().x);
                builder.append(" ");
                builder.append(a.getPosition().y);
                break;
            }
            case MOVEANIMAL:{
                Vector2d v = (Vector2d)((Object[]) object)[0];
                Animal a = (Animal)((Object[]) object)[1];
                builder.append(a.getAnimalID());
                builder.append(" ");
                builder.append(v.x);
                builder.append(" ");
                builder.append(v.y);
                builder.append(" ");
                builder.append(a.getPosition().x);
                builder.append(" ");
                builder.append(a.getPosition().y);
                break;
            }
            case CREATEWORLD:{
                JungleMap j = (JungleMap) object;
                builder.append(j.getMapCorners().upperRight.x);
                builder.append(" ");
                builder.append(j.getMapCorners().upperRight.y);
                break;
            }
            default:{
                throw new RuntimeException("excuse me, wtf?!");
            }
        }
        return builder.toString();
    }
}
