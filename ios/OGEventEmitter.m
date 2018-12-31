#import "OGEventEmitter.h"

@implementation OGEventEmitter
{
    bool hasListeners;
}

// Will be called when this module's first listener is added.
-(void)startObserving {
    hasListeners = YES;
    // Set up any upstream listeners or background tasks as necessary
}

// Will be called when this module's last listener is removed, or on dealloc.
-(void)stopObserving {
    hasListeners = NO;
    // Remove upstream listeners, stop unnecessary background tasks
}

RCT_EXPORT_MODULE();

+ (id)allocWithZone:(NSZone *)zone {
    static OGEventEmitter *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [super allocWithZone:zone];
    });
    return sharedInstance;
}

- (NSArray<NSString *> *)supportedEvents
{
  return @[@"onPlaybackEnd"];
}

- (void)itemDidFinishPlaying:(NSNotification *) notification
{
    if (hasListeners) { // Only send events if anyone is listening
        @try {
            [self sendEventWithName:@"onPlaybackEnd" body:notification.userInfo];
        } @catch (NSException *exception) {
            NSLog(@"%@", exception.reason);
        }
    }
}

@end
