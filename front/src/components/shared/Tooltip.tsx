type TooltipProps = {
  text: string;
  maxLen?: number;
};

const Tooltip = ({ text, maxLen }: TooltipProps) => {
  const stripText = maxLen ? text.slice(0, maxLen) + "..." : text;
  return (
    <div className="tooltip tooltip-bottom tooltip-accent" data-tip={text}>
      {stripText}
    </div>
  );
};

export default Tooltip;
