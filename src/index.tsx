import { requireNativeComponent, ViewStyle } from 'react-native';

type DatarizeLoggerProps = {
  color: string;
  style: ViewStyle;
};

export const DatarizeLoggerViewManager = requireNativeComponent<DatarizeLoggerProps>(
'DatarizeLoggerView'
);

export default DatarizeLoggerViewManager;
