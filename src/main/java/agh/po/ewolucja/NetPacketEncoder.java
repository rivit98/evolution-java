package agh.po.ewolucja;

enum NetOptions{
    PUTGRASS,
    PUTANIMAL,
    DELGRASS,
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
            case PUTGRASS:
            case DELGRASS: {
                Grass g = (Grass) object;
                builder.append(g.getPosition().x);
                builder.append(" ");
                builder.append(g.getPosition().y);
                break;
            }
            case PUTANIMAL:
            case DELANIMAL:
            case MOVEANIMAL: {
                Animal a = (Animal) object;
                builder.append(a.getAnimalId());
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
