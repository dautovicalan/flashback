import { useMemo } from "react";
import { AxisOptions, Chart } from "react-charts";
import { LogAction, LogEntry } from "../../types/types";

type StatisticChartProps = {
  groupedData: Record<LogAction, LogEntry[]> | null;
};

const StatisticChart = ({ groupedData }: StatisticChartProps) => {
  const chartData = useMemo(() => {
    if (!groupedData || Object.keys(groupedData).length === 0) return [];

    return Object.entries(groupedData).map(([logAction, logs]) => ({
      label: logAction,
      data: logs.map((log) => ({
        logAction: log.logAction,
        count: logs.length,
      })),
    }));
  }, [groupedData]);

  const primaryAxis = useMemo(
    (): AxisOptions<{ logAction: LogAction; count: number }> => ({
      getValue: (datum) => datum.logAction,
    }),
    []
  );

  const secondaryAxes = useMemo(
    (): AxisOptions<{ logAction: LogAction; count: number }>[] => [
      {
        getValue: (datum) => datum.count,
        min: 0,
      },
    ],
    []
  );

  return (
    <div className="w-full h-96 mt-5 card bg-white shadow-xl">
      <Chart
        options={{
          data: chartData,
          primaryAxis: primaryAxis,
          secondaryAxes: secondaryAxes,
        }}
      />
    </div>
  );
};

export default StatisticChart;
