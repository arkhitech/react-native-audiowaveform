#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface OGEventEmitter : RCTEventEmitter <RCTBridgeModule>
- (void)itemDidFinishPlaying:(NSNotification *) notification;

@end