import gearth.extensions.Extension;
import gearth.extensions.ExtensionInfo;
import gearth.extensions.parsers.HEntity;
import gearth.protocol.HMessage;
import gearth.protocol.HPacket;

import java.util.Timer;
import java.util.TimerTask;

@ExtensionInfo(
        Title = "ISeeDeadPeople",
        Description = "Everyone's a ghost",
        Version = "1.0",
        Author = "Zzuhw"
)

class ISeeDeadPeople extends Extension {

  public static void main(String[] args) {
    new ISeeDeadPeople(args).run();
  }

  public ISeeDeadPeople(String[] args) {
    super(args);
  }

  @Override
  protected void initExtension() {
    intercept(HMessage.Direction.TOCLIENT, "Users", this::onUsers);;
  }


  private void onUsers(HMessage hMessage) {
    User[] users = User.parse(hMessage.getPacket());

    for (User user : users) {
      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          sendToClient(new HPacket("AvatarEffect", HMessage.Direction.TOCLIENT, user.getIndex(), 13, 0));
        }
      }, 500);
    }
  }

  public static class User extends HEntity {
    public User(HPacket packet) {
      super(packet);
    }

    public static User[] parse(HPacket packet) {
      User[] entities = new User[packet.readInteger()];

      for ( int i = 0; i < entities.length; ++i ) {
        entities[i] = new User(packet);
      }
      return entities;
    }
  }

}
