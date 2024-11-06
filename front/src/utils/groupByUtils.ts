import { LogAction, LogEntry } from "../types/types";

export const groupByLogAction = (logs: LogEntry[]) => {
  return logs.reduce((acc, log) => {
    if (!acc[log.logAction]) {
      acc[log.logAction] = [];
    }
    acc[log.logAction].push(log);
    return acc;
  }, {} as Record<LogAction, LogEntry[]>);
};
