#import "OGEventEmitter.h"

@implementation OGEventEmitter

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents
{
  return @[@"onPlaybackEnd"];
}

- (void)itemDidFinishPlaying:(NSNotification *) notification
{
  NSString *eventName = notification.userInfo[@"name"];
  [self sendEventWithName:@"onPlaybackEnd" body:@{@"name": eventName}];
}

@end