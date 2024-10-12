package dev.vrba.discord.worldle.api.controller;

import dev.vrba.discord.worldle.api.model.SubscribedChannel;
import dev.vrba.discord.worldle.api.service.SubscribedChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bot/subscribed-channels")
@RequiredArgsConstructor
public class SubscribedChannelController {

    private final SubscribedChannelService service;

    @GetMapping
    public ResponseEntity<List<String>> listChannels() {
        final List<String> response = service.getSubscribedChannels()
                .stream()
                .map(SubscribedChannel::id)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Void> subscribe(@PathVariable("id") String id) {
        service.subscribe(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable("id") String id) {
        service.unsubscribe(id);
        return ResponseEntity.ok().build();
    }
}
