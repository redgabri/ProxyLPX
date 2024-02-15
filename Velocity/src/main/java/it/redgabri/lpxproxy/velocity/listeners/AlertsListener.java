package it.redgabri.lpxproxy.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import it.redgabri.lpxproxy.velocity.ProxyLPX;

public class AlertsListener {

    @Subscribe
    public void onPluginMessage(PluginMessageEvent e){
        byte[] data = e.getData();
        String message = new String(data);
        if(e.getIdentifier().equals(ProxyLPX.getInstance().getChannel())){
            if(message.startsWith("#LPX#")){
                e.setResult(PluginMessageEvent.ForwardResult.handled());
                ProxyLPX.getInstance().getAlertsManager().sendAlert(message);
            }
        }
    }
}
